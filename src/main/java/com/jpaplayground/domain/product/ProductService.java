package com.jpaplayground.domain.product;

import com.jpaplayground.domain.product.dto.ProductCreateRequest;
import com.jpaplayground.domain.product.dto.ProductResponse;
import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.exception.NotFoundException;
import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberRepository;
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
	private final MemberRepository memberRepository;

	//	@Cacheable(key = "#pageable.pageSize+' '+#pageable.pageNumber", cacheNames = "productList")
	/* TODO: SliceImpl 에 기본생성자가 없어서, 여기서 바로 MySlice로 변환해야겠다 */
	public Slice<ProductResponse> findAll(Pageable pageable) {
		return productRepository.findProductsByDeletedFalse(pageable).map(ProductResponse::new);
	}

	@Transactional
	public ProductResponse save(ProductCreateRequest request, Long memberId) {
		// TODO : 서비스단 validation
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
		Product product = Product.builder()
			.name(request.getName())
			.price(request.getPrice())
			.createdBy(member)
			.build();

		return new ProductResponse(productRepository.save(product));
	}

	@Transactional
	public ProductResponse delete(Long productId) {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
		product.changeDeletedState(true);
		return new ProductResponse(product);
	}
}
