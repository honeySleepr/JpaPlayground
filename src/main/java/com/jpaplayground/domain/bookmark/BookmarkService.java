package com.jpaplayground.domain.bookmark;

import com.jpaplayground.domain.bookmark.dto.BookmarkResponse;
import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.ProductRepository;
import com.jpaplayground.domain.product.dto.ProductResponse;
import com.jpaplayground.domain.product.exception.ProductException;
import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberRepository;
import com.jpaplayground.global.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

	private final BookmarkRepository bookmarkRepository;
	private final ProductRepository productRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public BookmarkResponse save(Long productId, Long memberId) { // select 쿼리 2
		Product product = findProduct(productId);
		Member member = findMember(memberId);

		member.getBookmarks().stream()
			.filter(bookmark -> bookmark.matchesProduct(product))
			.findFirst()
			.ifPresentOrElse(bookmark -> {},
				() -> {
					Bookmark bookmark = new Bookmark(product, member);
					bookmarkRepository.save(bookmark);
				});

		return new BookmarkResponse(product);
	}

	@Transactional
	public BookmarkResponse save2(Long productId, Long memberId) { // select 쿼리 3
		Bookmark bookmark = bookmarkRepository.findByProductIdAndMemberId(productId, memberId)
			.orElseGet(() -> {
				Product product = findProduct(productId);
				Member member = findMember(memberId);
				return bookmarkRepository.save(new Bookmark(product, member));
			});

		return new BookmarkResponse(bookmark.getProduct());
	}

	@Transactional
	public BookmarkResponse delete(Long productId, Long memberId) { // select 쿼리 2
		Product product = findProduct(productId);
		Member member = findMember(memberId);

		member.getBookmarks().stream()
			.filter(bookmark -> bookmark.matchesProduct(product))
			.findFirst()
			.ifPresent(bookmark -> {
				bookmark.delete();
				bookmarkRepository.delete(bookmark);
			});

		return new BookmarkResponse(product);
	}

	@Transactional
	public BookmarkResponse delete2(Long productId, Long memberId) { // select 쿼리 1
		Bookmark bookmark = bookmarkRepository.findByProductIdAndMemberId(productId, memberId).orElseThrow();
		Product product = bookmark.getProduct();
		bookmark.delete();
		bookmarkRepository.delete(bookmark);

		return new BookmarkResponse(product);
	}

	public Slice<ProductResponse> findAllByMemberId(Long memberId, Pageable pageable) {
		Slice<Bookmark> bookmarks = bookmarkRepository.findAllByMemberId(memberId, pageable);
		return bookmarks.map(bookmark -> new ProductResponse(bookmark.getProduct()));
	}

	private Product findProduct(Long productId) {
		return productRepository.findProductById(productId)
			.orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));
	}

	private Member findMember(Long memberId) {
		return memberRepository.findWithBookmarksById(memberId)
			.orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
	}

}
