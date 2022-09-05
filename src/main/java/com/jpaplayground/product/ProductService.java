package com.jpaplayground.product;

import com.jpaplayground.product.dto.ProductCreateRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

	private final ProductRepository repository;

	/* TODO: findAll() 대신 페이징 처리된 조회 메서드 만들기 */
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
		product.delete(true);
		return product;
	}
}
