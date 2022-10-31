package com.jpaplayground.product;

import com.jpaplayground.TestData;
import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.ProductService;
import com.jpaplayground.domain.product.dto.ProductCreateRequest;
import com.jpaplayground.domain.product.dto.ProductResponse;
import com.jpaplayground.domain.product.exception.ProductException;
import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.member.Member;
import java.util.List;
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
	Member member1;

	@BeforeEach
	void init() {
		testData.init();
		allProducts = testData.getAllProducts();
		member1 = testData.getAllMembers().get(0);

	}

	@Test
	@DisplayName("제품을 등록하면 제품이 저장된다")
	void save() {
		// given
		ProductCreateRequest request = new ProductCreateRequest("한무무", 149_000);
		Long id = member1.getId();

		// when
		ProductResponse savedProduct = productService.save(request);

		// then
		assertThat(savedProduct.getName()).isEqualTo(request.getName());
		assertThat(savedProduct.getPrice()).isEqualTo(request.getPrice());
	}

	@Test
	@DisplayName("존재하지 않는 product를 삭제하려고 하면 예외가 발생한다")
	void delete_error() {
		// given
		Long id = Long.MAX_VALUE;

		// when
		// then
		assertThatThrownBy(() -> productService.delete(id)).isInstanceOf(ProductException.class)
			.hasMessage(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
	}

	@Nested
	@DisplayName("Product 조회 시")
	class FindAll {

		@Test
		@DisplayName("Paging이 적용된 제품 목록을 반환한다")
		void paging() {
			//given
			int page = 1;
			int size = 3;
			PageRequest pageRequest = PageRequest.of(page, size);

			//when
			Slice<ProductResponse> slice = productService.findAll(pageRequest);

			// then
			List<ProductResponse> content = slice.getContent();
			assertThat(content.size()).isEqualTo(size);
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
			Slice<ProductResponse> slice = productService.findAll(pageable);

			// then
			assertThat(slice.getNumberOfElements()).isEqualTo(numberOfNonDeletedProducts);
		}

	}

}
