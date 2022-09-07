package com.jpaplayground.product;

import com.jpaplayground.product.dto.ProductCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

	private final ProductRepository repository;

	/* TODO: findAll() 대신 페이징 처리된 조회 메서드 만들기 */
	public Slice<Product> findAll(Pageable pageable) {
		return repository.findProductsBy(pageable);
	}

	@Transactional
	public Product save(ProductCreateRequest request) {
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
