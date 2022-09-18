package com.jpaplayground.conversation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.jpaplayground.conversation.dto.ConversationCreateRequest;
import com.jpaplayground.product.Product;
import com.jpaplayground.product.ProductRepository;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConversationServiceIntegrationTest {

	@Autowired
	ConversationService service;
	@Autowired
	ConversationRepository conversationRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	EntityManager entityManager;

	@AfterEach
	void teardown() {
		conversationRepository.deleteAll();
		productRepository.deleteAll();
	}

	@Test
	@DisplayName("리뷰 등록 요청을 하면 DB에 저장된다")
	void add() {
		// given
		Product product = Product.of("한무무", 149_000);
		Product savedProduct = productRepository.save(product);
		int initialSize = conversationRepository.findAll().size();
		ConversationCreateRequest request = new ConversationCreateRequest(savedProduct.getId(), "리뷰를 추가한다");
		entityManager.clear(); /* 1차 캐시의 영향을 배제하는게 좋을 것 같아서 */

		// when
		Conversation savedConversation = service.save(request);
		// then
		Conversation foundConversation = conversationRepository.findById(savedConversation.getId()).get();

		assertAll(
			() -> assertThat(foundConversation).usingRecursiveComparison().isEqualTo(savedConversation),
			() -> assertThat(conversationRepository.findAll()).hasSize(initialSize + 1)
		);
	}

}
