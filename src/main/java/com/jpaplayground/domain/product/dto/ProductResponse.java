package com.jpaplayground.domain.product.dto;

import com.jpaplayground.domain.product.Product;
import lombok.Getter;

@Getter
public class ProductResponse {

	private final Long id;
	private final String name;
	private final Integer price;
	private final Long sellerId;
	private final boolean isReserved;

	public ProductResponse(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.price = product.getPrice();
		this.sellerId = product.getSeller().getId(); /* id 조회 시에는 프록시가 초기화되지 않는다! */
		this.isReserved = product.isReserved();
	}
}
