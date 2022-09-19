package com.jpaplayground.conversation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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
	void save() {
		// given
		Long productId = 7L;
		ConversationCreateRequest request = new ConversationCreateRequest(productId, "대화를 시작하지");
		Product product = Product.of("제품", 10_000);

		Conversation conversation = Conversation.of(request.getContent(), product);
		given(productRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(product));
		given(conversationRepository.save(any(Conversation.class))).willReturn(conversation);

		// when
		Conversation savedConversation = service.save(request);

		// then
		then(productRepository).should(times(1)).findById(request.getProductId());
		then(conversationRepository).should(times(1)).save(refEq(conversation));

		assertThat(savedConversation.getContent()).isEqualTo(request.getContent());
	}

}
