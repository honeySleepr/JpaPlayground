package com.jpaplayground.domain.product.dto;

import com.jpaplayground.domain.product.Product;
import lombok.Getter;

@Getter
public class ProductDeleteResponse extends ProductResponse {

	private final Boolean deleted;

	public ProductDeleteResponse(Product product) {
		super(product);
		this.deleted = product.getDeleted();
	}
}
