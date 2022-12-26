package com.jpaplayground.domain.purchaserecord;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PurchaseRecordRepository extends JpaRepository<PurchaseRecord, Long> {

	@EntityGraph(attributePaths = {"product", "product.bookmarks"})
	@Query("select p from PurchaseRecord p where p.buyer.id = :memberId")
	Slice<PurchaseRecord> findAllByBuyerId(@Param("memberId") Long memberId, Pageable pageable);
}
