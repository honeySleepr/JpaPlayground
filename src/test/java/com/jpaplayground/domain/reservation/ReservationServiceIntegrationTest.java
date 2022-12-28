package com.jpaplayground.domain.reservation;

import com.jpaplayground.TestData;
import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.ProductRepository;
import com.jpaplayground.domain.product.ProductStatus;
import com.jpaplayground.domain.product.exception.ProductException;
import com.jpaplayground.domain.reservation.dto.ReservationCreateRequest;
import com.jpaplayground.domain.reservation.dto.ReservationResponse;
import com.jpaplayground.domain.reservation.dto.ReservationUpdateRequest;
import com.jpaplayground.domain.reservation.exception.ReservationException;
import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.member.Member;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ReservationServiceIntegrationTest {

	@Autowired
	ReservationService reservationService;

	@Autowired
	ProductRepository productRepository;
	@Autowired
	ReservationRepository reservationRepository;
	@Autowired
	TestData testData;
	Product product;
	Product reservedProduct1;
	Product reservedProduct2;
	Product reservedProduct3;
	Member seller;
	Member buyer;
	Member thirdPerson;
	int buyerReservationCount;
	int thirdPersonReservationCount;
	int sellerReservationCount;

	@BeforeEach
	void init() {
		testData.init();
		seller = testData.getSeller();
		buyer = testData.getBuyer();
		thirdPerson = testData.getThirdPerson();

		List<Product> allProducts = testData.getAllProducts();
		product = allProducts.get(4);

		buyerReservationCount = 2;
		thirdPersonReservationCount = 1;
		sellerReservationCount = 0;

		reservedProduct1 = allProducts.get(5);
		Reservation reservation1 = new Reservation(buyer, reservedProduct1, LocalDateTime.now());
		reservedProduct2 = allProducts.get(6);
		Reservation reservation2 = new Reservation(buyer, reservedProduct2, LocalDateTime.now());
		reservedProduct3 = allProducts.get(7);
		Reservation reservation3 = new Reservation(thirdPerson, reservedProduct3, LocalDateTime.now()); // thirdPerson
		reservationRepository.save(reservation1);
		productRepository.save(reservedProduct1);
		reservationRepository.save(reservation2);
		productRepository.save(reservedProduct2);
		reservationRepository.save(reservation3);
		productRepository.save(reservedProduct3);
	}

	@Nested
	@DisplayName("Reservation 등록 테스트")
	class CreateTest {

		@Test
		@DisplayName("판매자가 자신이 등록한 제품의 예약을 요청을 하면 예약이 생성되고, 제품 상태가 예약중으로 변경된다")
		void create() {
			// given
			Long productId = product.getId();
			LocalDateTime now = LocalDateTime.now();
			ReservationCreateRequest request = new ReservationCreateRequest(buyer.getId(), now);
			if (product.getStatus() == ProductStatus.RESERVED) {
				fail("이미 예약중인 제품");
			}

			// when
			ReservationResponse response = reservationService.save(request, productId, seller.getId());

			// then
			Product foundProduct = productRepository.findById(productId).get();

			assertAll(
				() -> assertThat(response.getTimeToMeet()).isEqualTo(now),
				() -> assertThat(response.getBuyerId()).isEqualTo(buyer.getId()),
				() -> assertThat(foundProduct.getStatus()).isEqualTo(ProductStatus.RESERVED)
			);
		}

		@Test
		@DisplayName("이미 예약된 제품에 예약 요청을 하면 예외가 발생한다")
		void create_already_reserved() {
			// given
			LocalDateTime now = LocalDateTime.now();
			ReservationCreateRequest request = new ReservationCreateRequest(buyer.getId(), now);

			// when
			// then
			assertThatThrownBy(() -> reservationService.save(request, reservedProduct1.getId(), seller.getId()))
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
			assertThatThrownBy(() -> reservationService.save(request, product.getId(), buyer.getId()))
				.isInstanceOf(ProductException.class)
				.hasMessage(ErrorCode.NOT_SELLER.getMessage());
		}
	}

	@Nested
	@DisplayName("Reservation 조회 테스트")
	class FindTest {

		@Test
		@DisplayName("로그인 된 member의 예약 정보를 조회한다")
		void find() {
			//given
			Long buyerId = buyer.getId();
			Long thirdPersonId = thirdPerson.getId();
			Long sellerId = seller.getId();
			PageRequest pageRequest = PageRequest.ofSize(20);

			// when
			List<ReservationResponse> content1 = reservationService.findAllByMemberId(buyerId, pageRequest)
				.getContent();
			List<ReservationResponse> content2 = reservationService.findAllByMemberId(thirdPersonId, pageRequest)
				.getContent(); // thirdPerson 예약 조회
			List<ReservationResponse> content3 = reservationService.findAllByMemberId(sellerId, pageRequest)
				.getContent();

			// then
			assertThat(content1).hasSize(buyerReservationCount);
			assertThat(content2).hasSize(thirdPersonReservationCount);
			assertThat(content3).hasSize(sellerReservationCount);
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
			ReservationResponse response = reservationService.update(reservedProduct1.getId(), request, seller.getId());

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
			assertThatThrownBy(() -> reservationService.update(reservedProduct1.getId(), request, buyer.getId()))
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
			assertThatThrownBy(() -> reservationService.update(product.getId(), request, seller.getId()))
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
			//given
			Long reservedProductId = reservedProduct1.getId();
			Long sellerId = seller.getId();

			// when
			ReservationResponse response = reservationService.delete(reservedProductId, sellerId);

			// then
			assertThat(response.getProductId()).isEqualTo(reservedProductId);
			assertThat(response.getSellerId()).isEqualTo(sellerId);
		}

		@Test
		@DisplayName("판매자가 아닌 member가 예약 삭제 요청 시 예외가 발생한다")
		void delete_not_seller() {
			// when
			// then
			assertThatThrownBy(() -> reservationService.delete(reservedProduct1.getId(), buyer.getId()))
				.isInstanceOf(ProductException.class)
				.hasMessage(ErrorCode.NOT_SELLER.getMessage());
		}

		@Test
		@DisplayName("존재하지 않는 예약에 삭제 요청을 하면 예외가 발생한다")
		void delete_no_reservation() {
			// when
			// then
			assertThatThrownBy(() -> reservationService.delete(product.getId(), seller.getId()))
				.isInstanceOf(ReservationException.class)
				.hasMessage(ErrorCode.RESERVATION_NOT_FOUND.getMessage());
		}
	}
}
