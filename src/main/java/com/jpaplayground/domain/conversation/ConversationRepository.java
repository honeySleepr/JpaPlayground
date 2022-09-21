package com.jpaplayground.domain.conversation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

	@EntityGraph(attributePaths = "product")
	Optional<Conversation> findById(Long aLong);

	List<Conversation> findByProductId(Long productId);
}
