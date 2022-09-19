package com.jpaplayground.product;

import com.jpaplayground.product.dto.ProductCreateRequest;
import com.jpaplayground.product.dto.ProductResponse;
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
		// TODO : 입력 validation을 서비스단에서만 하는건 어떨까?
		return repository.save(request.toEntity());
	}

	@Transactional
	public Product delete(Long id) {
		Product product = repository.findById(id).orElseThrow();
		product.delete();
		return product;
	}
}
