package com.jpaplayground.domain.bookmark;

import com.jpaplayground.domain.bookmark.dto.BookmarkResponse;
import com.jpaplayground.domain.product.dto.ProductResponse;
import com.jpaplayground.global.login.LoginMemberId;
import com.jpaplayground.global.response.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class BookmarkController {

	private final BookmarkService bookmarkService;

	@PostMapping("/bookmarks/products/{productId}")
	public ResponseEntity<BookmarkResponse> create(@PathVariable Long productId, @LoginMemberId Long memberId) {
		BookmarkResponse response = bookmarkService.save(productId, memberId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@DeleteMapping("/bookmarks/products/{productId}")
	public ResponseEntity<BookmarkResponse> delete(@PathVariable Long productId, @LoginMemberId Long memberId) {
		return ResponseEntity.ok(bookmarkService.delete(productId, memberId));
	}

	@GetMapping("members/bookmarks/products")
	public PagingResponse<ProductResponse> findAllByMember(@LoginMemberId Long memberId, Pageable pageable) {
		return new PagingResponse<>(bookmarkService.findAllByMemberId(memberId, pageable));
	}
}
