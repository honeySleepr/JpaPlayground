package com.jpaplayground.domain.reservation;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReservationResponse {

	private final Long id;
	private final LocalDateTime createdAt;
	private final LocalDateTime timeToMeet;
	private final Long buyerId;

	public ReservationResponse(Reservation reservation) {
		this.id = reservation.getId();
		this.createdAt = reservation.getCreatedAt();
		this.timeToMeet = reservation.getTimeToMeet();
		this.buyerId = reservation.getBuyer().getId();
	}
}
