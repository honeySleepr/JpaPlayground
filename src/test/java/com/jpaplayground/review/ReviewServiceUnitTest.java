package com.jpaplayground.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jpaplayground.product.Product;
import com.jpaplayground.product.ProductRepository;
import com.jpaplayground.review.dto.ReviewCreateRequest;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceUnitTest {

	@InjectMocks
	ReviewService service;
	@Mock
	ReviewRepository reviewRepository;
	@Mock
	ProductRepository productRepository;

	@Test
	@DisplayName("Review 등록 요청을 받으면 Review 객체로 변환하여 저장한다")
	void add() {
		// given
		Long productId = 7L;
		ReviewCreateRequest request = new ReviewCreateRequest(productId, "리뷰를 달자");
		Product product = Product.of("제품", 10_000);
		Review review = request.toEntity(product);
		when(productRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(product));
		when(reviewRepository.save(any(Review.class))).thenReturn(review);

		// when
		Review savedReview = service.save(request);

		// then
		assertThat(savedReview).usingRecursiveComparison().ignoringFields("id", "product")
			.isEqualTo(request);
	}

	/* TODO: 이게 과연 맞는가.. repository와 종속적인 테스트는 단위테스트를 하는게 의미가 없는 것 같아서 일단 통합테스트로 다시 후퇴 */
	/* 아래 처럼 repository의 메서드 호출로 잘 이어지는지만 확인하려 해도 stubbing을 해야해서, 의미가 없는 것 같다. */
	@Test
	@DisplayName("Review 전체 조회")
	void add_other_version() {
		// given
		Long productId = 7L;
		ReviewCreateRequest request = new ReviewCreateRequest(productId, "리뷰를 달자");
		Product product = Product.of("제품", 10_000);
		Review review = request.toEntity(product);
		when(productRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(product));
		when(reviewRepository.save(any(Review.class))).thenReturn(review);

		// when
		service.save(request);

		// then
		verify(productRepository).findById(any(Long.class));
		verify(reviewRepository).save(any(Review.class));
	}

}
