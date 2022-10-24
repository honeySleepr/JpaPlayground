package com.jpaplayground.domain.reservation;

import com.jpaplayground.global.login.LoginMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	@PostMapping("/reservations")
	public ResponseEntity<ReservationResponse> create(@RequestBody ReservationCreateRequest request,
		@LoginMemberId Long sellerId) {
		return ResponseEntity.ok(reservationService.create(request, sellerId));
	}
}
