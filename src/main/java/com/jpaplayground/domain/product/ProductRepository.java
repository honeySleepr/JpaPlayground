package com.jpaplayground.domain.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation">Query
 * methods</a>
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

	Slice<Product> findProductsByDeletedFalse(Pageable pageable);
}
