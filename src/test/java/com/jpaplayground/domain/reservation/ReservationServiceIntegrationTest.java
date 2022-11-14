package com.jpaplayground.domain.reservation;

import com.jpaplayground.TestData;
import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.ProductRepository;
import com.jpaplayground.domain.product.exception.ProductException;
import com.jpaplayground.domain.reservation.dto.ReservationCreateRequest;
import com.jpaplayground.domain.reservation.dto.ReservationResponse;
import com.jpaplayground.domain.reservation.exception.ReservationException;
import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.member.Member;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ReservationServiceIntegrationTest {

	@Autowired
	ReservationService reservationService;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	TestData testData;
	Product notReservedProduct;
	Product reservedProduct;
	Member seller;
	Member buyer;
	Member thirdPerson;

	@BeforeEach
	void init() {
		testData.init();
		notReservedProduct = testData.getAllProducts().get(4);
		reservedProduct = testData.getReservedProduct();
		seller = testData.getSeller();
		buyer = testData.getBuyer();
		thirdPerson = testData.getThirdPerson();
	}

	@Nested
	@DisplayName("Reservation 등록 테스트")
	class CreateTest {

		@Test
		@DisplayName("판매자가 자신이 등록한 제품의 예약을 요청을 하면 예약이 생성된다")
		void create() {
			// given
			LocalDateTime now = LocalDateTime.now();
			ReservationCreateRequest request = new ReservationCreateRequest(buyer.getId(), now);

			// when
			ReservationResponse response = reservationService.create(request, notReservedProduct.getId(),
				seller.getId());

			// then
			assertThat(response.getTimeToMeet()).isEqualTo(now);
			assertThat(response.getBuyerId()).isEqualTo(buyer.getId());

			Product foundProduct = productRepository.findByIdAndDeletedFalse(notReservedProduct.getId()).get();
			Reservation reservation = foundProduct.getReservation();
			assertThat(reservation.getTimeToMeet()).isEqualTo(now);
			assertThat(reservation.getBuyer().getId()).isEqualTo(buyer.getId());
		}

		@Test
		@DisplayName("이미 예약된 제품에 예약 요청을 하면 예외가 발생한다")
		void create_already_reserved() {
			// given
			LocalDateTime now = LocalDateTime.now();
			ReservationCreateRequest request = new ReservationCreateRequest(buyer.getId(), now);

			// when
			// then
			assertThatThrownBy(() -> reservationService.create(request, reservedProduct.getId(), seller.getId()))
				.isInstanceOf(ReservationException.class)
				.hasMessage(ErrorCode.RESERVED.getMessage());
		}

		@Test
		@DisplayName("판매자가 아닌 member가 제품에 예약을 요청을 하면 예외가 발생한다")
		void create_not_seller() {
			// given
			LocalDateTime now = LocalDateTime.now();
			ReservationCreateRequest request = new ReservationCreateRequest(buyer.getId(), now);

			// when

			// then
			assertThatThrownBy(() -> reservationService.create(request, notReservedProduct.getId(), buyer.getId()))
				.isInstanceOf(ProductException.class)
				.hasMessage(ErrorCode.NOT_SELLER.getMessage());
		}
	}

	@Nested
	@DisplayName("Reservation 조회 테스트")
	class FindTest {

		@Test
		@DisplayName("판매자 또는 구매자가 제품의 예약 정보 조회 요청 시 예약 정보를 반환한다")
		void find() {
			// given

			// when

			// then

		}
	}
}
