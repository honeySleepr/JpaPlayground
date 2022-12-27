package com.jpaplayground.domain.reservation;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

	@Transactional
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("delete from Reservation r where r.product.id = :productId")
	void deleteAllByProductId(@Param("productId") Long productId);

	@EntityGraph(attributePaths = "product")
	Optional<Reservation> findByProductId(Long productId);

}
