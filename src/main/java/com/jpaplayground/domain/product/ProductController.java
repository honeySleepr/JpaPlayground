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

	private final ProductService productService;

	@GetMapping("/products")
	public PagingResponse<ProductResponse> findAll(Pageable pageable) {
		return new PagingResponse<>(productService.findAll(pageable));
	}

	@GetMapping("/members/products")
	public PagingResponse<ProductResponse> findAllByMember(@LoginMemberId Long memberId, Pageable pageable) {
		return new PagingResponse<>(productService.findAllByMemberId(memberId, pageable));
	}

	@PostMapping("/products")
	public ResponseEntity<ProductResponse> add(@Valid @RequestBody ProductCreateRequest request) {
		ProductResponse save = productService.save(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(save);
	}

	@DeleteMapping("/products/{productId}")
	public ResponseEntity<ProductResponse> delete(@PathVariable Long productId, @LoginMemberId Long memberId) {
		return ResponseEntity.ok(productService.delete(memberId, productId));
	}

	@PatchMapping("/products/{productId}")
	public ResponseEntity<ProductResponse> update(@PathVariable Long productId,
												  @Valid @RequestBody ProductUpdateRequest request,
												  @LoginMemberId Long memberId) {
		return ResponseEntity.ok(productService.update(memberId, productId, request));
	}
}
