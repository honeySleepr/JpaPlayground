package com.jpaplayground.domain.product;

import com.jpaplayground.TestData;
import com.jpaplayground.domain.product.dto.ProductCreateRequest;
import com.jpaplayground.domain.product.dto.ProductResponse;
import com.jpaplayground.domain.product.dto.ProductUpdateRequest;
import com.jpaplayground.domain.product.exception.ProductException;
import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.member.Member;
import java.util.List;
import javax.validation.ConstraintViolationException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceIntegrationTest {

	@Autowired
	ProductService productService;
	@Autowired
	TestData testData;
	List<Product> allProducts;
	Product product;
	Member seller;
	Member buyer;

	@BeforeEach
	void init() {
		testData.init();
		allProducts = testData.getAllProducts();
		product = allProducts.get(4);
		seller = testData.getSeller();
		buyer = testData.getBuyer();
	}

	@Nested
	@DisplayName("Product 등록 테스트")
	class Create {

		@Test
		@DisplayName("제품을 등록하면 제품이 저장된다")
		void save() {
			// given
			ProductCreateRequest request = new ProductCreateRequest("한무무", 149_000);

		// when
		ProductResponse savedProduct = productService.save(request);

		// then
		assertThat(savedProduct.getName()).isEqualTo(request.getName());
		assertThat(savedProduct.getPrice()).isEqualTo(request.getPrice());
	}

	@Test
	@DisplayName("판매자가 자신의 product를 삭제 요청을 하면 product가 삭제된다")
	void delete() {
		// given
		Long sellerId = seller.getId();

		// when
		ProductResponse response = productService.delete(sellerId, product.getId());

		// then
		assertThat(response.getId()).isEqualTo(product.getId());
		assertThat(response.getSellerId()).isEqualTo(sellerId);
	}

	@Test
	@DisplayName("판매자가 아닌 member가 product 삭제 요청을 하면 예외가 발생한다")
	void delete_not_seller() {
		// given
		Long buyerId = buyer.getId();

		// when
		// then
		assertThatThrownBy(() -> productService.delete(buyerId, product.getId())).isInstanceOf(ProductException.class)
			.hasMessage(ErrorCode.NOT_SELLER.getMessage());
	}

	@Test
	@DisplayName("존재하지 않는 product를 삭제하려고 하면 예외가 발생한다")
	void delete_error() {
		// given
		Long nonExistingProductId = Long.MAX_VALUE;

		// when
		// then
		assertThatThrownBy(() -> productService.delete(seller.getId(), nonExistingProductId)).isInstanceOf(
				ProductException.class)
			.hasMessage(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
	}

	@Nested
	@DisplayName("Product 조회 시")
	class FindTest {

		@Test
		@DisplayName("Paging이 적용된 제품 목록을 반환한다")
		void paging() {
			//given
			int page = 1;
			int size = 3;
			PageRequest pageRequest = PageRequest.of(page, size);

			//when
			Slice<ProductResponse> slice = productService.findAllNotDeletedProducts(pageRequest);

			// then
			List<ProductResponse> content = slice.getContent();
			assertThat(content).hasSize(size);
		}

		@Test
		@DisplayName("삭제된 제품은 조회되지 않는다")
		void filter_deleted() {
			// given
			int numberOfTotalProducts = allProducts.size();
			int numberOfNonDeletedProducts = (int) allProducts.stream()
				.filter(product -> !product.getDeleted())
				.count();
			Pageable pageable = Pageable.ofSize(numberOfTotalProducts);

			// when
			Slice<ProductResponse> slice = productService.findAllNotDeletedProducts(pageable);

			// then
			assertThat(slice.getNumberOfElements()).isEqualTo(numberOfNonDeletedProducts);
		}

	}

	@Test
	@DisplayName("제품의 모든 필드를 수정 요청을 하면 모든 필드가 수정된다")
	void update_all_fields() {
		// given
		ProductUpdateRequest request = new ProductUpdateRequest("수정제품", 1234567);
		Long sellerId = seller.getId();
		@Nested
		@DisplayName("Product 수정 테스트")
		class Update {

			@Test
			@DisplayName("제품의 모든 필드를 수정 요청을 하면 모든 필드가 수정된다")
			void update_all_fields() {
				// given
				Product product = allProducts.get(4);
				ProductUpdateRequest request = new ProductUpdateRequest("수정제품", 1234567);
				Long sellerId = seller.getId();

				// when
				ProductResponse updatedProduct = productService.update(sellerId, product.getId(), request);

				// then
				assertThat(updatedProduct.getName()).isEqualTo(request.getName());
				assertThat(updatedProduct.getPrice()).isEqualTo(request.getPrice());
			}

			@Test
			@DisplayName("제품의 필드 하나만을 수정 요청을 하면 해당 필드만 수정된다")
			void update_some_fields() {
				// given
				Integer oldPrice = product.getPrice();
				ProductUpdateRequest request = new ProductUpdateRequest("수정제품", null);
				Long sellerId = seller.getId();

				// when
				ProductResponse updatedProduct = productService.update(sellerId, product.getId(), request);

				// then
				assertThat(updatedProduct.getName()).isEqualTo(request.getName());
				assertThat(updatedProduct.getPrice()).isEqualTo(oldPrice);
			}

			@Test
			@DisplayName("판매자가 아닌 member가 product 수정 요청을 하면 예외가 발생한다")
			void update_not_seller() {
				// given
				ProductUpdateRequest request = new ProductUpdateRequest("수정제품", null);
				Long buyerId = buyer.getId();

				// when

				// then
				assertThatThrownBy(() -> productService.update(buyerId, product.getId(), request))
					.isInstanceOf(ProductException.class)
					.hasMessage(ErrorCode.NOT_SELLER.getMessage());
			}
		}
