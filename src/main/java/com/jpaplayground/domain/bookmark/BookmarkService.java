package com.jpaplayground.domain.bookmark;

import com.jpaplayground.domain.bookmark.dto.BookmarkResponse;
import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.ProductRepository;
import com.jpaplayground.domain.product.exception.ProductException;
import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberRepository;
import com.jpaplayground.global.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
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
	public BookmarkResponse save(Long productId, Long memberId) {
		Product product = findProduct(productId);
		Member member = findMember(memberId);

		Bookmark bookmark = member.getBookmarks().stream()
			.filter(bm -> bm.matchesProduct(product.getId()))
			.findFirst()
			.orElseGet(() -> bookmarkRepository.save(new Bookmark(product, member)));

		return new BookmarkResponse(bookmark, product.getBookmarkCount());
	}

	private Product findProduct(Long productId) {
		return productRepository.findByIdAndDeletedFalse(productId)
			.orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));
	}

	private Member findMember(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
	}

	// TODO: 조회 - member의 북마크 조회 -> memberController에서 진행

}
