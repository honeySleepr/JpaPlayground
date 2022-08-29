package com.jpaplayground.product;

import com.jpaplayground.product.dto.ProductAddRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

	private final ProductService service;

	@GetMapping("/products")
	public ResponseEntity<List<Product>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@PostMapping("/products")
	public ResponseEntity<Product> add(@RequestBody ProductAddRequest request) { // Todo : validation
		return ResponseEntity.status(HttpStatus.CREATED).body(service.add(request));
	}

}
