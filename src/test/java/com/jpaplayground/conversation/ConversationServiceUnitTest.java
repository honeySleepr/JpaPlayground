package com.jpaplayground.conversation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jpaplayground.conversation.dto.ConversationCreateRequest;
import com.jpaplayground.product.Product;
import com.jpaplayground.product.ProductRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * ProductServiceUnitTest와 마찬가지로 단순한 메서드는 단위 테스트를 하는게 의미가 없는것 같다
 */
@ExtendWith(MockitoExtension.class)
class ConversationServiceUnitTest {

	@InjectMocks
	ConversationService service;
	@Mock
	ConversationRepository conversationRepository;
	@Mock
	ProductRepository productRepository;

	@Test
	@DisplayName("Conversation을 등록한다")
	void add() {
		// given
		Long productId = 7L;
		ConversationCreateRequest request = new ConversationCreateRequest(productId, "리뷰를 달자");
		Product product = Product.of("제품", 10_000);
		Conversation conversation = request.toEntity(product);
		when(productRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(product));
		when(conversationRepository.save(any(Conversation.class))).thenReturn(conversation);

		// when
		Conversation savedConversation = service.save(request);

		// then
		assertThat(savedConversation.getContent()).isEqualTo(request.getContent());

	}

	@Test
	@DisplayName("Conversation을 등록한다 2")
	void add_other_version() {
		// given
		Long productId = 7L;
		ConversationCreateRequest request = new ConversationCreateRequest(productId, "리뷰를 달자");
		Product product = Product.of("제품", 10_000);
		Conversation conversation = request.toEntity(product);
		when(productRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(product));
		when(conversationRepository.save(any(Conversation.class))).thenReturn(conversation);

		// when
		service.save(request);

		// then
		verify(productRepository).findById(any(Long.class));
		verify(conversationRepository).save(any(Conversation.class));
	}

}
