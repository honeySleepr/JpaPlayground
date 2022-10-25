package com.jpaplayground.domain.reservation;

import com.jpaplayground.TestData;
import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.exception.ProductException;
import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.member.Member;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
	List<Product> allProducts;
	List<Member> allMembers;

	@BeforeEach
	void init() {
		testData.init();
		allProducts = testData.getAllProducts();
		allMembers = testData.getAllMembers();
	}

	@Test
	@DisplayName("판매자가 자신이 등록한 제품의 예약을 요청을 하면 예약이 생성된다")
	void create() {
		// given
		Member seller = allMembers.get(0);
		Member buyer = allMembers.get(1);
		Product product5 = allProducts.get(4);
		LocalDateTime now = LocalDateTime.now();
		ReservationCreateRequest request = new ReservationCreateRequest(product5.getId(), buyer.getId(), now);

		// when
		ReservationResponse response = reservationService.create(request, seller.getId());

		// then
		assertThat(response.getTimeToMeet()).isEqualTo(now);
		assertThat(response.getBuyerId()).isEqualTo(buyer.getId());
	}

	@Test
	@DisplayName("이미 예약된 제품에 예약 요청을 하면 예외가 발생한다")
	void create_already_reserved() {
		// given
		Member seller = allMembers.get(0);
		Member buyer = allMembers.get(1);
		Product reservedProduct = allProducts.get(1);
		LocalDateTime now = LocalDateTime.now();
		ReservationCreateRequest request = new ReservationCreateRequest(reservedProduct.getId(), buyer.getId(), now);

		// when

		// then
		assertThatThrownBy(() -> reservationService.create(request, seller.getId()))
			.isInstanceOf(ProductException.class)
			.hasMessage(ErrorCode.RESERVED.getMessage());
	}

	@Test
	@DisplayName("판매자가 아닌 member가 제품에 예약을 요청을 하면 예외가 발생한다")
	void create_not_seller() {
		// given
		Member buyer = allMembers.get(1);
		Product product5 = allProducts.get(4);
		LocalDateTime now = LocalDateTime.now();
		ReservationCreateRequest request = new ReservationCreateRequest(product5.getId(), buyer.getId(), now);

		// when

		// then
		assertThatThrownBy(() -> reservationService.create(request, buyer.getId()))
			.isInstanceOf(ProductException.class)
			.hasMessage(ErrorCode.NOT_SELLER.getMessage());
	}

}
