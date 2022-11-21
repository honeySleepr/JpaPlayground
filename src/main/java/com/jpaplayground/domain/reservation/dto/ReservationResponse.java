package com.jpaplayground.domain.reservation.dto;

import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.reservation.Reservation;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReservationResponse {

	private final Long id;
	private final Long productId;
	private final LocalDateTime timeToMeet;
	private final Long sellerId;
	private final Long buyerId;

	public ReservationResponse(Product product, Reservation reservation) {
		this.id = reservation.getId();
		this.productId = product.getId();
		this.timeToMeet = reservation.getTimeToMeet();
		this.sellerId = product.getSeller().getId();
		this.buyerId = reservation.getBuyer().getId();
	}
}
