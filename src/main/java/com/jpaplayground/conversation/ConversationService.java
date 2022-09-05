package com.jpaplayground.conversation;

import com.jpaplayground.conversation.dto.ConversationCreateRequest;
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
		return conversationRepository.save(request.toEntity(product));
	}

	/* TODO: paging 적용된 조회 메서드로 바꿀것*/
	public List<Conversation> findAll() {
		return conversationRepository.findAll();
	}
}
