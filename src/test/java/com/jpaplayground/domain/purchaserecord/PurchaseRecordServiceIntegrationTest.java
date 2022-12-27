package com.jpaplayground.domain.purchaserecord;

import com.jpaplayground.TestData;
import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.ProductRepository;
import com.jpaplayground.domain.product.ProductStatus;
import com.jpaplayground.domain.product.dto.ProductResponse;
import com.jpaplayground.domain.product.exception.ProductException;
import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.member.Member;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PurchaseRecordServiceIntegrationTest {

	@Autowired
	PurchaseRecordService purchaseRecordService;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	PurchaseRecordRepository purchaseRecordRepository;
	@Autowired
	TestData testData;

	Product soldProduct1;
	Product soldProduct2;
	Product soldProduct3;
	Product product;
	Member seller;
	Member buyer;
	Member thirdPerson;
	int buyerBuyCount;
	int thirdPersonBuyCount;

	@BeforeEach
	void init() {
		testData.init();
		List<Product> allProducts = productRepository.findAll();
		seller = testData.getSeller();
		buyer = testData.getBuyer();
		thirdPerson = testData.getThirdPerson();
		product = allProducts.get(3);

		soldProduct1 = allProducts.get(4);
		soldProduct2 = allProducts.get(5);
		soldProduct3 = allProducts.get(6);

		buyerBuyCount = 2;
		thirdPersonBuyCount = 1;
		PurchaseRecord purchaseRecord1 = new PurchaseRecord(buyer, soldProduct1);
		PurchaseRecord purchaseRecord2 = new PurchaseRecord(buyer, soldProduct2);
		PurchaseRecord purchaseRecord3 = new PurchaseRecord(thirdPerson, soldProduct3);
		purchaseRecordRepository.save(purchaseRecord1);
		productRepository.save(soldProduct1);
		purchaseRecordRepository.save(purchaseRecord2);
		productRepository.save(soldProduct2);
		purchaseRecordRepository.save(purchaseRecord3);
		productRepository.save(soldProduct3);

	}

	@Nested
	@DisplayName("판매 완료 등록 테스트")
	class CreateTest {

		@Test
		@DisplayName("판매자가 신청을 할 경우 제품이 판매완료 상태가 된다.")
		void sell() {
			// given
			Long sellerId = seller.getId();
			Long buyerId = buyer.getId();
			Long productId = product.getId();
			if (product.getStatus() == ProductStatus.SOLD) {
				fail("이미 판매 완료된 제품");
			}

			// when
			purchaseRecordService.sellProduct(sellerId, productId, buyerId);

			// then
			Product foundProduct = productRepository.findById(productId).get();
			assertThat(foundProduct.getStatus()).isEqualTo(ProductStatus.SOLD);
		}

		@Test
		@DisplayName("이미 판매 완료된 제품일 경우 예외가 발생한다")
		void sell_already_sold() {
			// given
			Long sellerId = seller.getId();
			Long buyerId = buyer.getId();
			Long soldProductId = soldProduct1.getId();

			// when
			// then
			assertThatThrownBy(() -> purchaseRecordService.sellProduct(sellerId, soldProductId, buyerId))
				.isInstanceOf(ProductException.class)
				.hasMessage(ErrorCode.PRODUCT_SOLD.getMessage());
		}

	}

	@Nested
	@DisplayName("구매한 제품 목록 조회 테스트")
	class FindAllBoughtProductsTest {

		@Test
		@DisplayName("구매 제품 조회 시 member가 구매한 제품만 조회된다")
		void findAll() {
			// given
			String name1 = soldProduct1.getName();
			String name2 = soldProduct2.getName();
			String name3 = soldProduct3.getName();
			PageRequest pageRequest = PageRequest.ofSize(20);
			Long buyerId = buyer.getId();
			Long thirdPersonId = thirdPerson.getId();

			// when
			Slice<ProductResponse> slice1 = purchaseRecordService.findProductsBoughtByMember(buyerId, pageRequest);
			Slice<ProductResponse> slice2 = purchaseRecordService.findProductsBoughtByMember(thirdPersonId,
				pageRequest);

			// then
			List<ProductResponse> content1 = slice1.getContent();
			List<ProductResponse> content2 = slice2.getContent();
			assertThat(content1).hasSize(buyerBuyCount);
			assertThat(content2).hasSize(thirdPersonBuyCount);
			assertThat(content1.get(0).getName()).isEqualTo(name1);
			assertThat(content1.get(1).getName()).isEqualTo(name2);
			assertThat(content2.get(0).getName()).isEqualTo(name3);
		}

	}
}
