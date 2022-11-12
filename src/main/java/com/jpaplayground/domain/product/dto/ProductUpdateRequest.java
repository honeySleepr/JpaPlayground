package com.jpaplayground.domain.product.dto;

import javax.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProductUpdateRequest {

	private String name;
	@PositiveOrZero
	private Integer price;

}
