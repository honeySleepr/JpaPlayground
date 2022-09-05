package com.jpaplayground.conversation.dto;

import com.jpaplayground.conversation.Conversation;
import com.jpaplayground.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConversationCreateRequest {

	private Long productId;
	private String content;

	public Conversation toEntity(Product product) {
		return Conversation.builder()
			.content(content)
			.product(product)
			.build();
	}
}
