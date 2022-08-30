package com.jpaplayground.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jpaplayground.product.dto.ProductAddRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

// TODO : 서비스 레이어에서 `단위 테스트`와 `통합 테스트`를 각각 진행하는건 이상한건가?
@SpringBootTest
@Transactional
@ActiveProfiles("bc")
class ProductServiceIntegrationTest {

	final ProductService service;

	final ProductRepository repository;

	final List<Product> products;

	@Autowired
	public ProductServiceIntegrationTest(ProductService service, ProductRepository repository) {
		this.service = service;
		this.repository = repository;
		this.products = List.of(
			Product.of("한무무", 149_000),
			Product.of("4K 모니터", 500_000),
			Product.of("연어회 500g", 15_000)
		);

	}

	@Test
	@DisplayName("제품 등록 요청을 받으면 db에 제품이 저장된다")
	void add() {

		// given
		ProductAddRequest request = new ProductAddRequest("한무무", 149_000);
		int initialSize = repository.findAll().size();

		// when
		Product savedProduct = service.add(request);

		// then
		Product foundProduct = repository.findById(savedProduct.getId()).orElseThrow();
		assertAll(
			() -> assertThat(foundProduct).usingRecursiveComparison()
				.ignoringFields("id").isEqualTo(request),
			() -> assertThat(repository.findAll()).hasSize(initialSize + 1)
		);

	}

	@Test
	@DisplayName("전체 제품 조회 시 제품 목록을 반환한다")
	void findAll() {
		/* testdata.sql에 저장된 값을 그대로 findAll 해서 개수가 일치하는지 검사하는 것 보단
		 * 테스트 시에 직접 데이터를 넣은 뒤 확인하는게 더 제대로 된 테스트 같아서 그렇게 하였다.
		 * 그래서 sql.init.mode=never 로 해놨는데, testdata.sql는 그럼 필요가 없는건가..? */
		// given
		repository.save(products.get(0));
		repository.save(products.get(1));
		repository.save(products.get(2));

		// when
		List<Product> foundProducts = service.findAll();

		// then
		/* foundProducts.get(i)가  products.get(i)와 일치하는지 검사 */
		foundProducts.forEach(foundProduct -> assertThat(foundProduct)
			.usingRecursiveComparison()
			.isEqualTo(products.get(foundProducts.indexOf(foundProduct))));
	}

}