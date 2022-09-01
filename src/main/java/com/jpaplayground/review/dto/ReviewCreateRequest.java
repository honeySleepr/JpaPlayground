package com.jpaplayground.review.dto;

import com.jpaplayground.product.Product;
import com.jpaplayground.review.Review;
import lombok.Getter;

@Getter
public class ReviewCreateRequest {

	private Long productId;
	private String content;

	public Review toEntity(Product product) {
		return Review.builder()
			.content(content)
			.product(product)
			.build();
	}
}
