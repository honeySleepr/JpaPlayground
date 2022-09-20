package com.jpaplayground.domain.product;

import com.jpaplayground.domain.product.dto.ProductCreateRequest;
import com.jpaplayground.domain.product.dto.ProductResponse;
import com.jpaplayground.global.exception.BusinessException;
import com.jpaplayground.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional()
public class ProductService {

	private final ProductRepository repository;


	public Slice<ProductResponse> findAll(Pageable pageable) {
		return repository.findProductsByDeletedFalse(pageable).map(ProductResponse::new);
	}

	@Transactional
	public Product save(ProductCreateRequest request) {
		// TODO : 서비스단 validation
		return repository.save(request.toEntity());
	}

	@Transactional
	public Product delete(Long productId) {
		Product product = repository.findById(productId)
			.orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
		product.delete();
		return product;
	}
}
