package com.jpaplayground.domain.bookmark.dto;

import com.jpaplayground.domain.bookmark.Bookmark;
import lombok.Getter;

@Getter
public class BookmarkResponse {

	private final Long productId;
	private final int bookmarkCount;

	public BookmarkResponse(Bookmark bookmark, int bookmarkCount) {
		this.productId = bookmark.getProduct().getId();
		this.bookmarkCount = bookmarkCount;
	}
}
