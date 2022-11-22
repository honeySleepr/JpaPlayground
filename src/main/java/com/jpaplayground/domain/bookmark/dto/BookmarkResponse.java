package com.jpaplayground.domain.bookmark.dto;

import com.jpaplayground.domain.product.Product;
import lombok.Getter;

@Getter
public class BookmarkResponse {

	private final Long productId;
	private final int bookmarkCount;

	public BookmarkResponse(Product product) {
		this.productId = product.getId();
		this.bookmarkCount = product.getBookmarkCount();
	}
}
