package com.jpaplayground.domain.purchaserecord;

import com.jpaplayground.domain.product.dto.ProductResponse;
import com.jpaplayground.global.login.LoginMemberId;
import com.jpaplayground.global.response.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PurchaseRecordController {

	private final PurchaseRecordService purchaseRecordService;

	@PostMapping("/purchase-records/products/{productId}")
	public ResponseEntity<PurchaseRecordResponse> sellProduct(@PathVariable Long productId,
															  @LoginMemberId Long memberId,
															  @RequestBody PurchaseRequestDto purchaseRequestDto) {
		PurchaseRecordResponse response = purchaseRecordService.sellProduct(memberId, productId,
			purchaseRequestDto.getBuyerId());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/members/purchase-records/products")
	public PagingResponse<ProductResponse> findProductsBoughtByMember(@LoginMemberId Long memberId, Pageable pageable) {
		return new PagingResponse<>(purchaseRecordService.findProductsBoughtByMember(memberId, pageable));
	}
}
