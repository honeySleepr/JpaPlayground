package com.jpaplayground.product;

import com.jpaplayground.product.dto.ProductCreateRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository repository;

	@Transactional(readOnly = true)
	public List<Product> findAll() {
		return repository.findAll();
	}

	@Transactional
	public Product add(ProductCreateRequest request) {
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
