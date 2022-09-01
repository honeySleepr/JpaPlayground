package com.jpaplayground.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.jpaplayground.product.dto.ProductAddRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {

	@InjectMocks
	ProductService service;

	@Mock
	ProductRepository repository;

	final List<Product> products;

	public ProductServiceUnitTest() {
		this.products = List.of(
			Product.of("한무무", 149_000),
			Product.of("4K 모니터", 500_000),
			Product.of("연어회 500g", 15_000)
		);
	}

	@Test
	@DisplayName("제품을 등록하면 db에 제품이 저장된다")
	void add() {
		/* Todo : 이게 테스트 제대로 된거 맞는지..?*/
		// given
		ProductAddRequest request = new ProductAddRequest("한무무", 149_000);
		when(repository.save(any(Product.class))).thenReturn(request.toEntity());

		// when
		Product savedProduct = service.add(request);

		// then
		assertAll(
			() -> assertThat(savedProduct).usingRecursiveComparison()
				.ignoringFields("id").isEqualTo(request)
		);
	}

	@Test
	@DisplayName("전체 제품 조회 시 제품 목록을 반환한다")
	void findAll() {
		/* testdata.sql에 저장된 값을 그대로 findAll 해서 개수가 일치하는지 검사하는 것 보단
		 * 테스트 시에 직접 데이터를 넣은 뒤 확인하는게 더 제대로 된 테스트 같아서 그렇게 하였다.
		 * 그래서 sql.init.mode=never 로 해놨는데, testdata.sql는 그럼 필요가 없는건가..? */
		// given
		when(repository.findAll()).thenReturn(products);

		// when
		List<Product> foundProducts = service.findAll();

		// then
		/* foundProducts.get(i)가  products.get(i)와 일치하는지 검사 */
		foundProducts.forEach(foundProduct -> assertThat(foundProduct)
			.usingRecursiveComparison()
			.isEqualTo(products.get(foundProducts.indexOf(foundProduct))));
	}

}
