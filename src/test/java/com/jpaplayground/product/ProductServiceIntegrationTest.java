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
	ProductService service;
	@Autowired
	ProductRepository repository;

	/**
	 * 테스트에서 @Transactional을 쓰지 않기 위함 ->
	 * <a href="https://tecoble.techcourse.co.kr/post/2020-08-31-jpa-transaction-test/">테스트 코드에서 @Transactional
	 * 주의하기</a>
	 */
	@AfterEach
	void tearDown() {
		repository.deleteAll();
	}

	@Test
	@DisplayName("제품을 등록하면 db에 제품이 저장된다")
	void save() {

		// given
		ProductCreateRequest request = new ProductCreateRequest("한무무", 149_000);
		int initialSize = repository.findAll().size();

		// when
		Product savedProduct = service.save(request);

		// then
		Product foundProduct = repository.findById(savedProduct.getId()).orElseThrow();
		assertAll(
			() -> assertThat(foundProduct).usingRecursiveComparison()
				.isEqualTo(savedProduct),
			() -> assertThat(repository.findAll()).hasSize(initialSize + 1)
		);

	}

	/* @DataJpaTest로 하는게 나을 것 같다 */
	@Nested
	@DisplayName("Product 조회 시")
	class FindAll {

		@Test
		@DisplayName("Paging이 적용된 제품 목록을 반환한다")
		void findAll_paged() {
			//given
			for (int i = 1; i <= 20; i++) {
				repository.save(Product.of(String.valueOf(i), 1000));
			}
			int page = 1;
			int size = 3;
			PageRequest pageRequest = PageRequest.of(page, size);

			//when
			Slice<ProductResponse> slice = service.findAll(pageRequest);

			// then
			List<ProductResponse> content = slice.getContent();
			int offset = page * size;

			assertThat(content.size()).isEqualTo(size);
			for (int i = 0; i < size; i++) {
				assertThat(content.get(i).getName()).isEqualTo(String.valueOf(i + offset + 1));
			}
		}

		@Test
		@DisplayName("삭제된 제품은 조회되지 않는다")
		void findAll_not_deleted() {
			// given
			repository.save(Product.of("1", 1000));
			repository.save(Product.of("2", 1000));
			repository.save(Product.of("3", 1000));
			long id = 2L;
			int size = repository.findAll().size();
			Pageable pageable = Pageable.ofSize(size);
			Product product = repository.findById(id).get();
			product.delete();
			repository.save(product);

			// when
			Slice<Product> slice = repository.findProductsByDeletedFalse(pageable);

			// then
			assertThat(slice.getNumberOfElements()).isEqualTo(size - 1);
		}

	}

}
