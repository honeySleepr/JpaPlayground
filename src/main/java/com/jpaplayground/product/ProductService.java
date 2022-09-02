package com.jpaplayground.product;

import com.jpaplayground.product.dto.ProductCreateRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository repository;

	public List<Product> findAll() {
		return repository.findAll();
	}

	public Product add(ProductCreateRequest request) {
		// TODO : 입력 validation을 서비스단에서만 해도 괜찮지 않을까..?
		return repository.save(request.toEntity());
	}
}
