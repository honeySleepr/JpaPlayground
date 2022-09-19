package com.jpaplayground.domain.product.dto;

import com.jpaplayground.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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
