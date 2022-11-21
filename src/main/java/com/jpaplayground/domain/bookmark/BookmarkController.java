package com.jpaplayground.domain.bookmark;

import com.jpaplayground.domain.bookmark.dto.BookmarkResponse;
import com.jpaplayground.global.login.LoginMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/{productId}")
public class BookmarkController {

	private final BookmarkService bookmarkService;

	@PostMapping("/bookmarks")
	public ResponseEntity<BookmarkResponse> create(@PathVariable Long productId, @LoginMemberId Long memberId) {
		return ResponseEntity.ok(bookmarkService.save(productId, memberId));
	}

}
