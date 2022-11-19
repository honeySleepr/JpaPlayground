package com.jpaplayground.domain.bookmark;

import com.jpaplayground.TestData;
import com.jpaplayground.domain.bookmark.dto.BookmarkResponse;
import com.jpaplayground.domain.product.Product;
import com.jpaplayground.global.member.Member;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookmarkServiceIntegrationTest {

	@Autowired
	BookmarkService bookmarkService;
	@Autowired
	TestData testData;

	Product notBookmarkedProduct;
	Product bookmarkedProduct;
	Member seller;
	Member buyer;
	Member thirdPerson;

	@BeforeEach
	void init() {
		testData.init();
		this.bookmarkedProduct = testData.getBookmarkedProduct();
		this.notBookmarkedProduct = testData.getAllProducts().get(4);
		this.seller = testData.getSeller();
		this.buyer = testData.getBuyer();
		this.thirdPerson = testData.getThirdPerson();
	}

	@Nested
	@DisplayName("Bookmark 생성 테스트")
	class CreateTest {

		@Test
		@DisplayName("북마크 생성을 요청 하면 북마크가 생성된다")
		void save() {
			// given
			Long productId = notBookmarkedProduct.getId();
			Long buyerId = buyer.getId();

			// when
			BookmarkResponse response = bookmarkService.save(productId, buyerId);

			// then
			assertThat(response.getProductId()).isEqualTo(productId);
			assertThat(response.getBookmarkCount()).isEqualTo(1);
		}

		@Test
		@DisplayName("한 제품에 여러명의 북마크를 등록할 수 있다")
		void save_multiple() {
			// given
			Long productId = bookmarkedProduct.getId();
			Long memberId = thirdPerson.getId();

			// when
			BookmarkResponse response = bookmarkService.save(productId, memberId);

			// then
			assertThat(response.getBookmarkCount()).isEqualTo(2);
		}

		@Test
		@DisplayName("한 제품에 member 당 하나의 북마크만 등록할 수 있다")
		void save_one_per_member() {
			// given
			Long productId = bookmarkedProduct.getId();
			Long memberId = buyer.getId();

			// when
			BookmarkResponse response = bookmarkService.save(productId, memberId);

			// then
			assertThat(response.getBookmarkCount()).isEqualTo(1); // 이미 같은 member의 북마크가 있으면 개수가 증가하지 않는다.
		}
	}
}
