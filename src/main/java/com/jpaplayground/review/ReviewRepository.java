package com.jpaplayground.review;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	@EntityGraph(attributePaths = "product")
	Optional<Review> findById(Long aLong);
}
