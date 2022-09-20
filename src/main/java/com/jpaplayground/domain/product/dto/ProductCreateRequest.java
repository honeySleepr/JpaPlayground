package com.jpaplayground.domain.product.dto;

import com.jpaplayground.domain.product.Product;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
	@Min(value = 0)
	private Integer price;

	public Product toEntity() {
		return Product.builder()
			.name(name)
			.price(price)
			.build();
	}
}
