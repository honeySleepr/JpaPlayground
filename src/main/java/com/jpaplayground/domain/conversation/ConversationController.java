package com.jpaplayground.domain.conversation;

import com.jpaplayground.domain.conversation.dto.ConversationCreateRequest;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConversationController {

	private final ConversationService service;

	/* Todo: Entity를 직접 API에 노출하지 않도록 별도의 Response객체 만들기 */
	@GetMapping("/conversations")
	public ResponseEntity<List<Conversation>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@PostMapping("/conversations")
	public ResponseEntity<Conversation> add(@Valid @RequestBody ConversationCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
	}

	/**
	 * @param conversationId
	 * @return ProductId와 삭제된 전체 채팅 목록
	 */
	@DeleteMapping("/conversations/{conversationId}")
	public ResponseEntity<List<Conversation>> delete(@PathVariable Long conversationId) {
		return ResponseEntity.ok(service.delete(conversationId));
	}

}
