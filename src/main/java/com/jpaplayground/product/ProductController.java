package com.jpaplayground.product;

import com.jpaplayground.common.MySlice;
import com.jpaplayground.product.dto.ProductCreateRequest;
import com.jpaplayground.product.dto.ProductResponse;
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
	public ResponseEntity<Product> add(@RequestBody ProductCreateRequest request) { // Todo : validation
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<Product> delete(@PathVariable Long id) {
		return ResponseEntity.ok(service.delete(id));
	}
}
