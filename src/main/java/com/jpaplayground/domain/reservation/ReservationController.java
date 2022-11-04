package com.jpaplayground.domain.reservation;

import com.jpaplayground.global.login.LoginMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	@PostMapping("/products/{productId}/reservations")
	public ResponseEntity<ReservationResponse> create(@PathVariable Long productId,
													  @RequestBody ReservationCreateRequest request,
													  @LoginMemberId Long sellerId) {
		return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(request, productId, sellerId));
	}
}
