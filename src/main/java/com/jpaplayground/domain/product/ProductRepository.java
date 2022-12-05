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
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

	// TODO: 상황에 맞게 필요한 Entity만 fetch join 해오는 여러 메서드로 나누기
	@EntityGraph(attributePaths = {"reservation", "seller", "bookmarks"})
	Optional<Product> findByIdAndDeletedFalse(Long id);

	Slice<Product> findProductsByDeletedFalse(Pageable pageable);

	@Query("select p from Product p " +
		   "where p.seller.id = :sellerId and p.deleted = false " +
		   "and (p.status = 'SELLING' or p.status = 'RESERVED')")
	Slice<Product> findSellingProductsBySellerId(@Param("sellerId") Long sellerId, Pageable pageable);
}
