package com.jpaplayground.domain.product.dto;

import com.jpaplayground.domain.product.Product;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {

	@NotBlank
	private String name;
	@NotNull
	@PositiveOrZero
	private Integer price;

	public Product toEntity() {
		return Product.builder()
			.name(name)
			.price(price)
			.build();
	}
}
