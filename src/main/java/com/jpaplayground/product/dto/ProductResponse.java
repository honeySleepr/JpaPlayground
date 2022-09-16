package com.jpaplayground.product.dto;

import com.jpaplayground.product.Product;
import lombok.Getter;

@Getter
public class ProductResponse {

	private Long id;
	private String name;
	private Integer price;

	public ProductResponse(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.price = product.getPrice();
	}
}
