package com.jpaplayground.domain.purchaserecord;

import com.jpaplayground.TestData;
import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.ProductRepository;
import com.jpaplayground.domain.product.ProductStatus;
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
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PurchaseRecordServiceIntegrationTest {

	@Autowired
	PurchaseRecordService purchaseRecordService;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	TestData testData;

	Product soldProduct;
	Product product;
	Member seller;
	Member buyer;

	@BeforeEach
	void init() {
		testData.init();
		List<Product> allProducts = testData.getAllProducts();
		soldProduct = allProducts.get(2);
		product = allProducts.get(3);
		seller = testData.getSeller();
		buyer = testData.getBuyer();
	}

	@Nested
	@DisplayName("판매 완료 신청 테스트")
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
			Long soldProductId = soldProduct.getId();

			// when
			// then
			assertThatThrownBy(() -> purchaseRecordService.sellProduct(sellerId, soldProductId, buyerId))
				.isInstanceOf(ProductException.class)
				.hasMessage(ErrorCode.PRODUCT_SOLD.getMessage());
		}

	}
}
