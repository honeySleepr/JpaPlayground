package com.jpaplayground.domain.reservation;

import com.jpaplayground.TestData;
import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.exception.ProductException;
import com.jpaplayground.domain.reservation.dto.ReservationCreateRequest;
import com.jpaplayground.domain.reservation.dto.ReservationResponse;
import com.jpaplayground.domain.reservation.dto.ReservationUpdateRequest;
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
	TestData testData;
	Product notReservedProduct;
	Product reservedProduct;
	Reservation reservation;
	Member seller;
	Member buyer;
	Member thirdPerson;

	@BeforeEach
	void init() {
		testData.init();
		notReservedProduct = testData.getAllProducts().get(4);
		reservedProduct = testData.getReservedProduct();
		reservation = testData.getReservation();
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
			// when
			ReservationResponse response1 = reservationService.findByProductId(reservedProduct.getId(),
				seller.getId());
			ReservationResponse response2 = reservationService.findByProductId(reservedProduct.getId(),
				buyer.getId());
			// then
			assertThat(response1.getBuyerId()).isEqualTo(buyer.getId());
			assertThat(response1.getSellerId()).isEqualTo(seller.getId());
			assertThat(response1.getProductId()).isEqualTo(reservedProduct.getId());
			assertThat(response2.getBuyerId()).isEqualTo(buyer.getId());
			assertThat(response2.getSellerId()).isEqualTo(seller.getId());
			assertThat(response2.getProductId()).isEqualTo(reservedProduct.getId());
		}

		@Test
		@DisplayName("판매자나 구매자가 아닌 member가 조회 요청 시 예외가 발생한다")
		void find_not_buyer_nor_sell() {
			// when
			// then
			assertThatThrownBy(() -> reservationService.findByProductId(reservedProduct.getId(), thirdPerson.getId()))
				.isInstanceOf(ReservationException.class)
				.hasMessage(ErrorCode.NOT_SELLER_NOR_BUYER.getMessage());
		}

		@Test
		@DisplayName("존재하지 않는 예약을 조회하면 예외가 발생한다")
		void find_no_reservation() {
			// when
			// then
			assertThatThrownBy(() -> reservationService.findByProductId(notReservedProduct.getId(), seller.getId()))
				.isInstanceOf(ReservationException.class)
				.hasMessage(ErrorCode.RESERVATION_NOT_FOUND.getMessage());
		}
	}

	@Nested
	@DisplayName("Reservation 수정 테스트")
	class UpdateTest {

		@Test
		@DisplayName("판매자가 예약 수정을 요청하면 예약이 수정된다")
		void update() {
			// given
			LocalDateTime newTime = LocalDateTime.of(2022, 12, 1, 12, 30);
			ReservationUpdateRequest request = new ReservationUpdateRequest(newTime);

			// when
			ReservationResponse response = reservationService.update(reservedProduct.getId(), request, seller.getId());

			// then
			assertThat(response.getTimeToMeet()).isEqualTo(newTime);
		}

		@Test
		@DisplayName("판매자가 아닌 member가 예약 수정을 요청하면 예외가 발생한다")
		void update_not_seller() {
			// given
			LocalDateTime newTime = LocalDateTime.of(2022, 12, 1, 12, 30);
			ReservationUpdateRequest request = new ReservationUpdateRequest(newTime);

			// when
			// then
			assertThatThrownBy(() -> reservationService.update(reservedProduct.getId(), request, buyer.getId()))
				.isInstanceOf(ProductException.class)
				.hasMessage(ErrorCode.NOT_SELLER.getMessage());
		}

		@Test
		@DisplayName("존재하지 않는 예약의 수정을 요청하면 예외가 발생한다.")
		void update_no_reservation() {
			// given
			LocalDateTime newTime = LocalDateTime.of(2022, 12, 1, 12, 30);
			ReservationUpdateRequest request = new ReservationUpdateRequest(newTime);

			// when
			// then
			assertThatThrownBy(() -> reservationService.update(notReservedProduct.getId(), request, seller.getId()))
				.isInstanceOf(ReservationException.class)
				.hasMessage(ErrorCode.RESERVATION_NOT_FOUND.getMessage());
		}
	}

	@Nested
	@DisplayName("Reservation 삭제 테스트")
	class DeleteTest {

		@Test
		@DisplayName("판매자가 예약 삭제 요청 시 예약을 삭제한다")
		void delete() {
			// when
			ReservationDeleteResponse response = reservationService.delete(reservedProduct.getId(), seller.getId());

			// then
			assertThat(response.isDeleted()).isTrue();
		}

		@Test
		@DisplayName("판매자가 아닌 member가 예약 삭제 요청 시 예외가 발생한다")
		void delete_not_seller() {
			// when
			// then
			assertThatThrownBy(() -> reservationService.delete(reservedProduct.getId(), buyer.getId()))
				.isInstanceOf(ProductException.class)
				.hasMessage(ErrorCode.NOT_SELLER.getMessage());
		}

		@Test
		@DisplayName("존재하지 않는 예약에 삭제 요청을 하면 예외가 발생한다")
		void delete_no_reservation() {
			// when
			// then
			assertThatThrownBy(() -> reservationService.delete(notReservedProduct.getId(), seller.getId()))
				.isInstanceOf(ReservationException.class)
				.hasMessage(ErrorCode.RESERVATION_NOT_FOUND.getMessage());
		}
	}
}
