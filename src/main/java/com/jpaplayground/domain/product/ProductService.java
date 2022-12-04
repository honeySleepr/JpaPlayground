package com.jpaplayground.domain.product;

import com.jpaplayground.domain.bookmark.BookmarkRepository;
import com.jpaplayground.domain.product.dto.ProductCreateRequest;
import com.jpaplayground.domain.product.dto.ProductResponse;
import com.jpaplayground.domain.product.dto.ProductUpdateRequest;
import com.jpaplayground.domain.product.exception.ProductException;
import com.jpaplayground.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

	private final ProductRepository productRepository;
	private final BookmarkRepository bookmarkRepository;

	//	@Cacheable(key = "#pageable.pageSize+' '+#pageable.pageNumber", cacheNames = "productList")
	/* TODO: SliceImpl 에 기본생성자가 없어서, 여기서 바로 MySlice로 변환해야겠다 */
	public Slice<ProductResponse> findAll(Pageable pageable) {
		return productRepository.findProductsByDeletedFalse(pageable).map(ProductResponse::new);
	}

	/* TODO: product 단건 상세 조회 */
	@Transactional
	public ProductResponse save(ProductCreateRequest request) {
		Product product = Product.of(request.getName(), request.getPrice());
		return new ProductResponse(productRepository.save(product));
	}

	@Transactional
	public ProductResponse delete(Long memberId, Long productId) {
		Product product = productRepository.findByIdAndDeletedFalse(productId)
			.orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

		product.verifySeller(memberId);
		product.delete();
		bookmarkRepository.deleteAllByProductIdInQuery(product.getId());
		// flushAutomatically = true를 추가하지 않으면, Product의 update 쿼리가 삭제된다!

		/* 이 방법이 귀찮긴 하지만 더 효율적인가?? innoDB에서는 FK도 인덱스가 만들어진다고 하니 차이 없을 것 같은데
		List<Long> collect = product.getBookmarks().stream().map(Bookmark::getId).collect(Collectors.toList());
		bookmarkRepository.deleteAllByIdInQuery(collect);
		*/
		return new ProductResponse(product);
	}

	@Transactional
	public ProductResponse update(Long memberId, Long productId, ProductUpdateRequest request) {
		Product product = productRepository.findByIdAndDeletedFalse(productId)
			.orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

		product.verifySeller(memberId);
		product.update(request.getName(), request.getPrice());
		return new ProductResponse(product);
	}
}
