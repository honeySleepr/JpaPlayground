package com.jpaplayground.domain.bookmark;

import com.jpaplayground.domain.bookmark.dto.BookmarkResponse;
import com.jpaplayground.global.login.LoginMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
		BookmarkResponse response = bookmarkService.save(productId, memberId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@DeleteMapping("/bookmarks")
	public ResponseEntity<BookmarkResponse> delete(@PathVariable Long productId, @LoginMemberId Long memberId) {
		return ResponseEntity.ok(bookmarkService.delete(productId, memberId));
	}
}
