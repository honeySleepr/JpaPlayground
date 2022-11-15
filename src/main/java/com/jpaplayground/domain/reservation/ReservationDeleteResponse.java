package com.jpaplayground.domain.reservation;

import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.reservation.dto.ReservationResponse;
import lombok.Getter;

@Getter
public class ReservationDeleteResponse extends ReservationResponse {

	private final boolean deleted;

	public ReservationDeleteResponse(Product product, Reservation reservation) {
		super(product, reservation);
		this.deleted = true;
	}
}
