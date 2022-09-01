package com.jpaplayground.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jpaplayground.product.Product;
import com.jpaplayground.product.ProductRepository;
import com.jpaplayground.review.dto.ReviewCreateRequest;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReviewServiceIntegrationTest {

	@Autowired
	ReviewService service;
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	EntityManager entityManager;

	@AfterEach
	void teardown() {
		reviewRepository.deleteAll();
		productRepository.deleteAll();
	}

	@Test
	@DisplayName("리뷰 등록 요청을 하면 DB에 저장된다")
	void add() {
		// given
		Product product = Product.of("한무무", 149_000);
		Product savedProduct = productRepository.save(product);
		int initialSize = reviewRepository.findAll().size();
		ReviewCreateRequest request = new ReviewCreateRequest(savedProduct.getId(), "리뷰를 추가한다");
		entityManager.clear(); /* 1차 캐시의 영향을 배제하는게 좋을 것 같아서 */

		// when
		Review savedReview = service.save(request);
		// then
		Review foundReview = reviewRepository.findById(savedReview.getId()).get();

		assertAll(
			() -> assertThat(foundReview).usingRecursiveComparison().isEqualTo(savedReview),
			() -> assertThat(reviewRepository.findAll()).hasSize(initialSize + 1)
		);
	}

}
