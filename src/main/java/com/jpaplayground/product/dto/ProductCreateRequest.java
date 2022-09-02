package com.jpaplayground.product.dto;

import com.jpaplayground.product.Product;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductCreateRequest {

	private String name;
	private Integer price;

	public Product toEntity() {
		return Product.builder()
			.name(name)
			.price(price)
			.build();
	}
}
