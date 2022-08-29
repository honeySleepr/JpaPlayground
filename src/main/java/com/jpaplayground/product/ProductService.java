package com.jpaplayground.product;

import com.jpaplayground.product.dto.ProductAddRequest;
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

	public Product add(ProductAddRequest request) {
		// TODO : dto에 대해서 서비스단에서도 다시 validation(컨트롤러에 대한 의존성을 가지지 않도록 하고, Unit 테스트 시에도 필요할듯)
		return repository.save(request.toEntity());
	}
}
