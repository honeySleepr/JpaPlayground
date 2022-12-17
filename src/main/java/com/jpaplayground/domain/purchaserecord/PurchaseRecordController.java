package com.jpaplayground.domain.purchaserecord;

import com.jpaplayground.global.login.LoginMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PurchaseRecordController {

	private final PurchaseRecordService purchaseRecordService;

	@PostMapping("/orders/products/{productId}")
	public ResponseEntity<PurchaseRecordResponse> sellProduct(@PathVariable Long productId,
															  @LoginMemberId Long memberId,
															  @RequestBody PurchaseRequestDto purchaseRequestDto) {
		PurchaseRecordResponse response = purchaseRecordService.sellProduct(memberId, productId,
			purchaseRequestDto.getBuyerId());
		return ResponseEntity.ok(response);
	}
}
