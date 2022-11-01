package com.jpaplayground.domain.product.dto;

import com.jpaplayground.domain.product.Product;
import lombok.Getter;

@Getter
public class ProductResponse {

	private final Long id;
	private final String name;
	private final Integer price;

	public ProductResponse(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.price = product.getPrice();
	}
}
