package com.jpaplayground.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation">Query
 * methods</a>
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

	/* 그냥 @Query를 사용하는게 더 가독성 좋은가..? */
	Slice<Product> findProductsByDeletedFalse(Pageable pageable);
}
