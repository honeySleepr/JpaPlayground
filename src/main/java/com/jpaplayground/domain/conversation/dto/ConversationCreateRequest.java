package com.jpaplayground.domain.conversation.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationCreateRequest {

	@NotBlank
	private Long productId;
	@NotBlank
	private String content;

}
