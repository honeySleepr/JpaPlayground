package com.jpaplayground.domain.product.dto;

import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.reservation.Reservation;
import lombok.Getter;

@Getter
public class ProductResponse {

	private final Long id;
	private final String name;
	private final Integer price;
	private final Long sellerId;
	private final Long reservationId;

	public ProductResponse(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.price = product.getPrice();
		this.sellerId = product.getSeller().getId(); /* id 조회 시에는 프록시가 초기화되지 않는다! */
		Reservation reservation = product.getReservation();
		if (reservation != null) {
			this.reservationId = reservation.getId();
		} else {
			this.reservationId = null;
		}
	}
}
