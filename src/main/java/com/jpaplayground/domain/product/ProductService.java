package com.jpaplayground.domain.product;

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

	//	@Cacheable(key = "#pageable.pageSize+' '+#pageable.pageNumber", cacheNames = "productList")
	/* TODO: SliceImpl 에 기본생성자가 없어서, 여기서 바로 MySlice로 변환해야겠다 */
	public Slice<ProductResponse> findAll(Pageable pageable) {
		return productRepository.findProductsByDeletedFalse(pageable).map(ProductResponse::new);
	}

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
		product.changeDeletedState(true);
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
