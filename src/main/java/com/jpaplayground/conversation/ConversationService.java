package com.jpaplayground.conversation;

import com.jpaplayground.conversation.dto.ConversationCreateRequest;
import com.jpaplayground.conversation.dto.ConversationDeleteRequest;
import com.jpaplayground.product.Product;
import com.jpaplayground.product.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConversationService {

	private final ConversationRepository conversationRepository;
	private final ProductRepository productRepository;

	@Transactional
	public Conversation save(ConversationCreateRequest request) {
		Product product = productRepository.findById(request.getProductId())
			.orElseThrow();// Todo : ProductNotFoundException
		return conversationRepository.save(Conversation.of(request.getContent(), product));
	}

	/* TODO: 지정된 Product에 대한 채팅만 조회 + paging 적용*/
	public List<Conversation> findAll() {
		return conversationRepository.findAll();
	}
	@Transactional
	public List<Conversation> delete(ConversationDeleteRequest request) {
		/* TODO: Login User의 Usertype을 받아와야한다 */
		//		List<Conversation> conversations = conversationRepository.findByProductId(request.productId).orElseThrow();
		//		conversations.forEach(conversation-> conversation.changeVisibilityTo(userType.counterPart()));
		return null;
	}
}
