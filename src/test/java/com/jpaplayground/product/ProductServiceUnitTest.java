package com.jpaplayground.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.jpaplayground.product.dto.ProductCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * 로직이 별거 없는 메서드들은 그저 repository의 메서드를 호출하는지 여부 밖에 테스트가 안된다<br> 그러므로 굳이 모든 메서드에 대한 테스트를 만들 필요 없이, 검증이 필요한 로직이 포함된 메서드만
 * 테스트하면 될 것 같다
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {

	@InjectMocks
	ProductService service;

	@Mock
	ProductRepository repository;

	/*  */
	@Test
	@DisplayName("제품을 등록하면 db에 제품이 저장된다")
	void save() {

		// given
		ProductCreateRequest request = new ProductCreateRequest("한무무", 149_000);
		when(repository.save(any(Product.class))).thenReturn(request.toEntity());

		// when
		Product savedProduct = service.save(request);

		// then
		assertAll(
			() -> assertThat(savedProduct.getName()).isEqualTo(request.getName()),
			() -> assertThat(savedProduct.getPrice()).isEqualTo(request.getPrice())
		);
	}

}
