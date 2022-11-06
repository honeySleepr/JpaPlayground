package com.jpaplayground.domain.reservation;

import com.jpaplayground.global.login.LoginMemberId;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/{productId}")
public class ReservationController {

	private final ReservationService reservationService;

	@PostMapping("/reservations")
	public ResponseEntity<ReservationResponse> create(@PathVariable Long productId,
													  @Valid @RequestBody ReservationCreateRequest request,
													  @LoginMemberId Long sellerId) {
		ReservationResponse body = reservationService.create(request, productId, sellerId);
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	@GetMapping("/reservations")
	public ResponseEntity<ReservationResponse> find(@PathVariable Long productId, @LoginMemberId Long memberId) {
		return ResponseEntity.ok(reservationService.findByProductId(productId, memberId));
	}
}
