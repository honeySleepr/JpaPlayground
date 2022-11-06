package com.jpaplayground.domain.reservation;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReservationResponse {

	private final Long id;
	private final Long productId;
	private final LocalDateTime createdAt;
	private final LocalDateTime timeToMeet;
	private final Long sellerId;
	private final Long buyerId;

	public ReservationResponse(Long productId, Reservation reservation) {
		this.id = reservation.getId();
		this.productId = productId;
		this.createdAt = reservation.getCreatedAt();
		this.timeToMeet = reservation.getTimeToMeet();
		this.sellerId = reservation.getSeller().getId();
		this.buyerId = reservation.getBuyer().getId();
	}
}
