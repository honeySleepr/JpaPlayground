package com.jpaplayground.domain.product;

import com.jpaplayground.domain.product.dto.ProductCreateRequest;
import com.jpaplayground.domain.product.dto.ProductResponse;
import com.jpaplayground.global.response.PagingResponse;
import com.jpaplayground.global.response.SingleResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

	private final ProductService service;

	@GetMapping("/products")
	public PagingResponse<ProductResponse> findAll(Pageable pageable) {
		return new PagingResponse<>(service.findAll(pageable));
	}

	@PostMapping("/products")
	public SingleResponse<ProductResponse> add(@Valid @RequestBody ProductCreateRequest request) {
		return new SingleResponse<>(service.save(request), HttpStatus.CREATED);
	}

	@DeleteMapping("/products/{productId}")
	public SingleResponse<ProductResponse> delete(@PathVariable Long productId) {
		return new SingleResponse<>(service.delete(productId));
	}

}
