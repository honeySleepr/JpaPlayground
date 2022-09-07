package com.jpaplayground.product;

import com.jpaplayground.product.dto.ProductCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

	/* Todo: Entity를 직접 API에 노출하지 않도록 별도의 Response객체 만들기 */
	@GetMapping("/products")
	public ResponseEntity<Slice<Product>> findAll(Pageable pageable) {
		return ResponseEntity.ok(service.findAll(pageable));
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
