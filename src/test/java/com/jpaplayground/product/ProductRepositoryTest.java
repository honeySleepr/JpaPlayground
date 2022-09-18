package com.jpaplayground.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@DataJpaTest
public class ProductRepositoryTest {

	@Autowired
	ProductRepository productRepository;
	@PersistenceContext
	EntityManager entityManager;

	@BeforeEach
	void initData() {
		List<Product> products = new ArrayList<>();
		for (int i = 1; i <= 20; i++) {
			products.add(Product.of(String.valueOf(i), 1000));
		}
		productRepository.saveAll(products);
		entityManager.clear();
	}

	/**
	 * Service 통합테스트에도 같은 테스트를 만들어보았는데, @DataJpaTest가 더 가볍고, 굳이 통합테스트로 검증해야할 로직은 아닌 것 같아서 여기서만 테스트하는게 적절한 것 같다.
	 */
	@Nested
	@DisplayName("Product 조회 시")
	class FindAll {

		@Test
		@DisplayName("Paging이 적용된 제품 목록을 반환한다")
		void findProductsByDeletedFalse_pageing() {

			//given
			int page = 2;
			int size = 3;
			int offset = page * size;
			PageRequest pageRequest = PageRequest.of(page, size);

			//when
			Slice<Product> slice = productRepository.findProductsByDeletedFalse(pageRequest);

			// then
			List<Product> content = slice.getContent();

			assertThat(content.size()).isEqualTo(size);
			for (int i = 0; i < size; i++) {
				assertThat(content.get(i).getName()).isEqualTo(String.valueOf(offset + 1 + i));
			}
		}

		@Test
		@DisplayName("삭제된 제품은 조회되지 않는다")
		void findAll_not_deleted() {
			// given
			List<Product> products = productRepository.findAll();
			int size = products.size();
			int deletedCount = 5;
			for (int i = 0; i < deletedCount; i++) {
				products.get(i).delete();
			}
			Pageable pageable = Pageable.ofSize(size);

			// when
			Slice<Product> slice = productRepository.findProductsByDeletedFalse(pageable);

			// then
			assertThat(slice.getNumberOfElements()).isEqualTo(size - deletedCount);

		}
	}

}
