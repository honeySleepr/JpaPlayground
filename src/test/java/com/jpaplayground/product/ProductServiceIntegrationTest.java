package com.jpaplayground.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jpaplayground.product.dto.ProductCreateRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
/**
 * Service 단에서 통합테스트와 단위테스트를 둘다 하는건 의미가 없는 것 같아서 단위테스트만 해볼 예정
 */
@SpringBootTest
class ProductServiceIntegrationTest {

	@Autowired
	ProductService service;
	@Autowired
	ProductRepository repository;

	/**
	 * 테스트에서 @Transactional을 쓰지 않기 위함 ->
	 * <a href="https://tecoble.techcourse.co.kr/post/2020-08-31-jpa-transaction-test/">테스트 코드에서 @Transactional 주의하기</a>
	 */
	@AfterEach
	void tearDown() {
		repository.deleteAll();
	}

	@Test
	@DisplayName("제품 등록 요청을 하면 db에 제품이 저장된다")
	void save() {

		// given
		ProductCreateRequest request = new ProductCreateRequest("한무무", 149_000);
		int initialSize = repository.findAll().size();

		// when
		Product savedProduct = service.add(request);

		// then
		Product foundProduct = repository.findById(savedProduct.getId()).orElseThrow();
		assertAll(
			() -> assertThat(foundProduct).usingRecursiveComparison()
				.isEqualTo(savedProduct),
			() -> assertThat(repository.findAll()).hasSize(initialSize + 1)
		);

	}

	/* TODO: findAll() 대신 페이징 처리된 조회 메서드 테스트 만들기 */
	@Test
	@DisplayName("전체 제품 조회 시 제품 목록을 반환한다")
	void findAll() {

	}

}
