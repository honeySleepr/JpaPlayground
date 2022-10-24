package com.jpaplayground.domain.reservation;

import com.jpaplayground.global.login.LoginMemberId;
import com.jpaplayground.global.response.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	@PostMapping("/reservations")
	public SingleResponse<ReservationResponse> create(@RequestBody ReservationCreateRequest request,
		@LoginMemberId Long sellerId) {
		return new SingleResponse<>(reservationService.create(request, sellerId));
	}
}
