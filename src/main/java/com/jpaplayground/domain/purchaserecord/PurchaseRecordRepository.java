package com.jpaplayground.domain.purchaserecord;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseRecordRepository extends CrudRepository<PurchaseRecord, Long> {

	@EntityGraph(attributePaths = {"product", "product.bookmarks"})
	Slice<PurchaseRecord> findAllByBuyerId(Long buyerId, Pageable pageable);
}
