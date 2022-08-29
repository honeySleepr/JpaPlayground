package com.jpaplayground.product.dto;

import com.jpaplayground.product.Product;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductAddRequest {

	private String name;
	private Integer price;

	public Product toEntity() {
		return Product.builder()
			.name(name)
			.price(price)
			.build();
	}
}
