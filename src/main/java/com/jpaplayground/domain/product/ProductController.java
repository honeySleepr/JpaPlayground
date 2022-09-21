package com.jpaplayground.domain.product;

import com.jpaplayground.domain.product.dto.ProductCreateRequest;
import com.jpaplayground.domain.product.dto.ProductResponse;
import com.jpaplayground.global.MySlice;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<MySlice<ProductResponse>> findAll(Pageable pageable) {
		return ResponseEntity.ok(new MySlice<>(service.findAll(pageable)));
	}

	@PostMapping("/products")
	public ResponseEntity<Product> add(@Valid @RequestBody ProductCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
	}

	@DeleteMapping("/products/{productId}")
	public ResponseEntity<Product> delete(@PathVariable Long productId) {
		return ResponseEntity.ok(service.delete(productId));
	}
}
