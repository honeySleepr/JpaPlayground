package com.jpaplayground.domain.product;

import com.jpaplayground.domain.product.dto.ProductCreateRequest;
import com.jpaplayground.domain.product.dto.ProductResponse;
import com.jpaplayground.domain.product.dto.ProductUpdateRequest;
import com.jpaplayground.global.login.LoginMemberId;
import com.jpaplayground.global.response.PagingResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
		return new PagingResponse<>(service.findAllNotDeletedProducts(pageable));
	}

	@PostMapping("/products")
	public ResponseEntity<ProductResponse> add(@Valid @RequestBody ProductCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
	}

	@DeleteMapping("/products/{productId}")
	public ResponseEntity<Void> delete(@PathVariable Long productId, @LoginMemberId Long memberId) {
		service.delete(memberId, productId);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/products/{productId}")
	public ResponseEntity<ProductResponse> update(@PathVariable Long productId,
												  @Valid @RequestBody ProductUpdateRequest request,
												  @LoginMemberId Long memberId) {
		return ResponseEntity.ok(service.update(memberId, productId, request));
	}
}
