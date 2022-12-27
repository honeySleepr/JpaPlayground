package com.jpaplayground.domain.bookmark;

import com.jpaplayground.TestData;
import com.jpaplayground.domain.bookmark.dto.BookmarkResponse;
import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.ProductService;
import com.jpaplayground.domain.product.dto.ProductResponse;
import com.jpaplayground.global.member.Member;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BookmarkServiceIntegrationTest {

	@Autowired
	BookmarkService bookmarkService;
	@Autowired
	ProductService productService;
	@Autowired
	BookmarkRepository bookmarkRepository;
	@Autowired
	TestData testData;

	Product product;
	Product bookmarkedProduct1;
	Product bookmarkedProduct2;
	Product bookmarkedProduct3;
	Member seller;
	Member buyer;
	Member thirdPerson;
	int buyerBookmarkCount;
	int thirdPersonBookmarkCount;

	@BeforeEach
	void init() {
		testData.init();
		this.seller = testData.getSeller();
		this.buyer = testData.getBuyer();
		this.thirdPerson = testData.getThirdPerson();
		List<Product> allProducts = testData.getAllProducts();
		this.product = allProducts.get(3);

		bookmarkedProduct1 = allProducts.get(4);
		bookmarkedProduct2 = allProducts.get(5);
		bookmarkedProduct3 = allProducts.get(6);
		buyerBookmarkCount = 2;
		thirdPersonBookmarkCount = 1;
		bookmarkRepository.save(new Bookmark(bookmarkedProduct1, buyer));
		bookmarkRepository.save(new Bookmark(bookmarkedProduct2, buyer));
		bookmarkRepository.save(new Bookmark(bookmarkedProduct3, thirdPerson));
	}

	@Nested
	@DisplayName("Bookmark 생성 테스트")
	class CreateTest {

		@Test
		@DisplayName("북마크 생성을 요청 하면 북마크가 생성된다")
		void save() {
			// given
			Long productId = product.getId();
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
			Long bookmarkedProductId = bookmarkedProduct1.getId();
			Long memberId = thirdPerson.getId();

			// when
			BookmarkResponse response = bookmarkService.save(bookmarkedProductId, memberId);

			// then
			assertThat(response.getBookmarkCount()).isEqualTo(2);
		}

		@Test
		@DisplayName("한 제품에 member 당 하나의 북마크만 등록할 수 있다")
		void save_one_per_member() {
			// given
			Long bookmarkedProductId = bookmarkedProduct1.getId();
			Long buyerId = buyer.getId();

			// when
			BookmarkResponse response = bookmarkService.save(bookmarkedProductId, buyerId);

			// then
			assertThat(response.getBookmarkCount()).isEqualTo(1); // 이미 같은 member의 북마크가 있으면 개수가 증가하지 않는다.
		}
	}

	@Nested
	@DisplayName("Bookmark 삭제 테스트")
	class DeleteTest {

		@Test
		@DisplayName("bookmark 삭제 요청 시 자신의 bookmark가 삭제된다")
		void delete() {
			// given
			Long bookmarkedProductId = bookmarkedProduct1.getId();
			Long buyerId = buyer.getId();
			int initialCount = bookmarkedProduct1.getBookmarkCount();

			// when
			BookmarkResponse response = bookmarkService.delete(bookmarkedProductId, buyerId);

			// then
			assertThat(response.getBookmarkCount()).isEqualTo(initialCount - 1);
		}

		@Test
		@DisplayName("bookmark 삭제 요청 시 다른 member의 bookmark는 삭제되지 않는다")
		void delete_not_yours() {
			// given
			Long bookmarkedProductId = bookmarkedProduct1.getId();
			int initialCount = bookmarkedProduct1.getBookmarkCount();
			Long thirdPersonId = thirdPerson.getId();

			// when
			BookmarkResponse response = bookmarkService.delete(bookmarkedProductId, thirdPersonId);

			// then
			assertThat(response.getBookmarkCount()).isEqualTo(initialCount);
		}
	}

	@Nested
	@DisplayName("Bookmark 조회 테스트")
	class FindTest {

		@Test
		@DisplayName("Bookmark 제품 조회 시 지정된 Member의 Bookmark 제품들만 조회된다")
		void find() {
			// given
			Long buyerId = buyer.getId();
			Long thirdPersonId = thirdPerson.getId();
			Pageable pageable = Pageable.ofSize(20);

			// when
			List<ProductResponse> content1 = bookmarkService.findList(buyerId, pageable).getContent();
			List<ProductResponse> content2 = bookmarkService.findList(thirdPersonId, pageable).getContent();

			// then
			assertThat(content1).hasSize(buyerBookmarkCount);
			assertThat(content1.get(0).getName()).isEqualTo(bookmarkedProduct1.getName());
			assertThat(content1.get(1).getName()).isEqualTo(bookmarkedProduct2.getName());
			assertThat(content2).hasSize(thirdPersonBookmarkCount);
			assertThat(content2.get(0).getName()).isEqualTo(bookmarkedProduct3.getName());
		}

		@Test
		@DisplayName("Bookmark 제품 조회 시 삭제된 제품은 조회되지 않는다")
		void find_not_deleted() {
			// given
			Long bookmarkedProductId = bookmarkedProduct1.getId();
			Long buyerId = buyer.getId();
			Pageable pageable = Pageable.ofSize(10);

			// when
			productService.delete(seller.getId(), bookmarkedProductId);

			// then
			List<ProductResponse> content = bookmarkService.findList(buyerId, pageable).getContent();
			assertThat(content).hasSize(buyerBookmarkCount - 1);
			assertThat(content.get(0).getName()).isEqualTo(bookmarkedProduct2.getName());
		}
	}
}
