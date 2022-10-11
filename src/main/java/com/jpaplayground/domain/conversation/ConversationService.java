package com.jpaplayground.domain.conversation;

import com.jpaplayground.domain.conversation.dto.ConversationCreateRequest;
import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.ProductRepository;
import com.jpaplayground.domain.product.exception.NotFoundException;
import com.jpaplayground.global.exception.ErrorCode;
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
			.orElseThrow(() -> new NotFoundException(ErrorCode.ENTITY_NOT_FOUND));
		return conversationRepository.save(Conversation.of(request.getContent(), product));
	}

	/* TODO: 지정된 Product에 대한 채팅만 조회 + paging 적용*/
	public List<Conversation> findAll() {
		return conversationRepository.findAll();
	}

	@Transactional
	public List<Conversation> delete(Long conversationId) {
		/* TODO: Login Member의 id를 받아와야한다 */
		return null;
	}
}
