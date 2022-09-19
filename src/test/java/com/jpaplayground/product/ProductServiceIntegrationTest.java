package com.jpaplayground.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jpaplayground.product.dto.ProductCreateRequest;
import com.jpaplayground.product.dto.ProductResponse;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

/**
 * Service 단에서 통합테스트와 단위테스트를 둘다 하는건 의미가 없는 것 같아서 단위테스트만 해볼 예정이었으나, 단위 테스트로 뭘 어떻게 테스트해야하는지 모르겠어서 다시 통합테스트로 후퇴
 */
@SpringBootTest
class ProductServiceIntegrationTest {

	@Autowired
	ProductService productService;
	@Autowired
	ProductRepository productRepository;

	/**
	 * 테스트에서 @Transactional을 쓰지 않기 위함 ->
	 * <a href="https://tecoble.techcourse.co.kr/post/2020-08-31-jpa-transaction-test/">테스트 코드에서 @Transactional
	 * 주의하기</a>
	 */
	@AfterEach
	void tearDown() {
		productRepository.deleteAll();
	}

	@Test
	@DisplayName("제품을 등록하면 db에 제품이 저장된다")
	void save() {

		// given
		ProductCreateRequest request = new ProductCreateRequest("한무무", 149_000);
		int initialSize = productRepository.findAll().size();

		// when
		Product savedProduct = productService.save(request);

		// then
		Product foundProduct = productRepository.findById(savedProduct.getId()).orElseThrow();
		assertAll(
			() -> assertThat(foundProduct).usingRecursiveComparison()
				.isEqualTo(savedProduct),
			() -> assertThat(productRepository.findAll()).hasSize(initialSize + 1)
		);

	}

	@Nested
	@DisplayName("Product 조회 시")
	class FindAll {

		@Test
		@DisplayName("Paging이 적용된 제품 목록을 반환한다")
		void findAll_paged() {
			//given
			for (int i = 1; i <= 20; i++) {
				productRepository.save(Product.of(String.valueOf(i), 1000));
			}
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
				assertThat(content.get(i).getName()).isEqualTo(String.valueOf(i + offset + 1));
			}
		}

		@Test
		@DisplayName("삭제된 제품은 조회되지 않는다")
		void findAll_not_deleted() {
			// given
			Product product1 = Product.of("1", 1000);
			product1.delete();
			productRepository.save(product1);
			productRepository.save(Product.of("2", 1000));
			productRepository.save(Product.of("3", 1000));

			int size = productRepository.findAll().size();
			Pageable pageable = Pageable.ofSize(size);

			// when
			Slice<ProductResponse> slice = productService.findAll(pageable);

			// then
			assertThat(slice.getNumberOfElements()).isEqualTo(size - 1);
		}

	}

}
