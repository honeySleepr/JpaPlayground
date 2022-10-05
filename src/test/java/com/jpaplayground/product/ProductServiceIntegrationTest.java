package com.jpaplayground.product;

import com.jpaplayground.TestData;
import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.ProductRepository;
import com.jpaplayground.domain.product.ProductService;
import com.jpaplayground.domain.product.dto.ProductCreateRequest;
import com.jpaplayground.domain.product.dto.ProductResponse;
import com.jpaplayground.domain.product.exception.ProductNotFoundException;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

/**
 * Service 단에서 통합테스트와 단위테스트를 둘다 하는건 의미가 없는 것 같아서 단위테스트만 해볼 예정이었으나, 단위 테스트로 뭘 어떻게 테스트해야하는지 모르겠어서 다시 통합테스트로 후퇴
 */
@SpringBootTest
@ActiveProfiles("test")
@Sql(value = "classpath:testdata.sql") /* 통합테스트에서는 테스트용 DB data 사용*/
class ProductServiceIntegrationTest {

	static List<Product> allProducts;
	static List<Product> deletedProducts;
	static List<Product> notDeletedProducts;

	@Autowired
	ProductService productService;
	@Autowired
	ProductRepository productRepository;

	@BeforeAll
	static void init() {
		allProducts = TestData.allProducts;
		deletedProducts = TestData.deletedProducts;
		notDeletedProducts = TestData.notDeletedProducts;
	}

	/**
	 * 테스트에서 @Transactional을 쓰지 않기 위함 ->
	 * <a href="https://tecoble.techcourse.co.kr/post/2020-08-31-jpa-transaction-test/">테스트 코드에서 @Transactional
	 * 주의하기</a>
	 */

	@Test /* TestData 가 repository의 데이터와 일치하는지도 검사를 해야할 것 같다. 그런데 이러면 배꼽이 더 커지는 것 같다.. */
	@DisplayName("testdata.sql로 삽입된 데이터와 TestData를 이용해 만든 데이터가 일치하는지 테스트한다")
	void testdata_integrity_test() {
		List<Product> savedProducts = productRepository.findAll();
		savedProducts.forEach(product -> assertThat(product).usingRecursiveComparison().ignoringFields("id")
			.isEqualTo(allProducts.get(savedProducts.indexOf(product))));
	}

	@Test
	@DisplayName("제품을 등록하면 제품이 저장된다")
	void save() {
		// given
		ProductCreateRequest request = new ProductCreateRequest("한무무", 149_000);
		int size = allProducts.size();

		// when
		Product savedProduct = productService.save(request);

		// then
		/* TODO: 테스트 결과를 검증할 때는 어쩔 수 없이 repository의 메서드를 쓰는게 맞는지 */
		Product foundProduct = productRepository.findById(savedProduct.getId()).get();

		assertThat(foundProduct).usingRecursiveComparison().isEqualTo(savedProduct);
		assertThat(productRepository.findAll()).hasSize(size + 1);
	}

	@Nested
	@DisplayName("Product 조회 시")
	class FindAllTest {

		@Test
		@DisplayName("Paging이 적용된 제품 목록을 반환한다")
		void findAll_paged() {
			//given
			int page = 1;
			int size = 3;
			int offset = page * size;
			PageRequest pageRequest = PageRequest.of(page, size);

			//when
			Slice<ProductResponse> slice = productService.findAll(pageRequest);

			// then
			List<ProductResponse> content = slice.getContent();

			assertThat(content.size()).isEqualTo(size);
			for (int i = 0; i < size; i++) {
				assertThat(content.get(i).getName()).isEqualTo(notDeletedProducts.get(i + offset).getName());
			}
		}

		@Test
		@DisplayName("삭제된 제품은 조회되지 않는다")
		void findAll_except_deleted() {
			// given
			int numberOfTotalProducts = allProducts.size();
			int numberOfDeletedProducts = deletedProducts.size();
			Pageable pageable = Pageable.ofSize(numberOfTotalProducts);

			// when
			Slice<ProductResponse> slice = productService.findAll(pageable);

			// then
			assertThat(slice.getNumberOfElements()).isEqualTo(numberOfTotalProducts - numberOfDeletedProducts);
		}

	}

	@Test
	@DisplayName("존재하지 않는 product를 삭제하려고 하면 예외가 발생한다")
	void delete_error() {
		// given
		Long id = allProducts.size() + 100L;

		// when
		// then
		assertThatThrownBy(() -> productService.delete(id)).isInstanceOf(ProductNotFoundException.class);
	}

}
