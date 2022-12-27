package com.jpaplayground.domain.product;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation">Query
 * methods</a>
 *
 * <h2> where p.deleted = false 필수 </h2>
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

	@EntityGraph(attributePaths = {"seller", "bookmarks"})
	@Query("select p from Product p where p.id = :productId and p.deleted = false")
	Optional<Product> findProductById(@Param("productId") Long productId);

	@Query("select p from Product p where p.deleted = false")
	Slice<Product> findAllProducts(Pageable pageable);

	@Query("select p from Product p where p.seller.id = :sellerId and p.deleted = false")
	Slice<Product> findProductsBySellerId(@Param("sellerId") Long sellerId, Pageable pageable);
}
