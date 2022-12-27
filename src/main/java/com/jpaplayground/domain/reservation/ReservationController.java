package com.jpaplayground.domain.reservation;

import com.jpaplayground.domain.reservation.dto.ReservationCreateRequest;
import com.jpaplayground.domain.reservation.dto.ReservationResponse;
import com.jpaplayground.domain.reservation.dto.ReservationUpdateRequest;
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
public class ReservationController {

	private final ReservationService reservationService;

	@PostMapping("/reservations/products/{productId}")
	public ResponseEntity<ReservationResponse> create(@PathVariable Long productId,
													  @Valid @RequestBody ReservationCreateRequest request,
													  @LoginMemberId Long memberId) {
		ReservationResponse body = reservationService.save(request, productId, memberId);
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	@PatchMapping("/reservations/products/{productId}")
	public ResponseEntity<ReservationResponse> updateTimeToMeet(@PathVariable Long productId,
																@RequestBody ReservationUpdateRequest request,
																@LoginMemberId Long memberId) {
		return ResponseEntity.ok(reservationService.update(productId, request, memberId));
	}

	@DeleteMapping("/reservations/products/{productId}")
	public ResponseEntity<ReservationResponse> delete(@PathVariable Long productId,
													  @LoginMemberId Long memberId) {
		return ResponseEntity.ok(reservationService.delete(productId, memberId));
	}

	@GetMapping("/members/reservations/products")
	public PagingResponse<ReservationResponse> findAllByMember(@LoginMemberId Long memberId, Pageable pageable) {
		return new PagingResponse<>(reservationService.findAllByMemberId(memberId, pageable));
	}
}
