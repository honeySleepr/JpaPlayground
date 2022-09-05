package com.jpaplayground.product;

import static org.assertj.core.api.Assertions.assertThat;

import com.jpaplayground.TestData;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProductRepositoryTest {

	@Autowired
	ProductRepository productRepository;
	@PersistenceContext
	EntityManager entityManager;

	@BeforeEach
	void initData() {
		productRepository.saveAll(TestData.products);
		entityManager.clear();
	}

	@Test
	@DisplayName("삭제 요청을 하면 product가 삭제 처리 된다")
	void delete() {

		// given
		Long id = 1L;
		Product product = productRepository.findById(id).orElseThrow();
		boolean originalState = product.isDeleted();

		// when
		product.delete(true);
		entityManager.flush();
		entityManager.clear();

		// then
		Product foundProduct = productRepository.findById(id).orElseThrow();

		assertThat(originalState).isFalse();
		assertThat(foundProduct.isDeleted()).isTrue();
	}

	// TODO: 제품 조회(페이징) 시 delete=true 처리된 product는 조회되지 않는다

}
