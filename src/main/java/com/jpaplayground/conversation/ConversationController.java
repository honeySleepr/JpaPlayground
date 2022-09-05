package com.jpaplayground.conversation;

import com.jpaplayground.conversation.dto.ConversationCreateRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConversationController {

	private final ConversationService service;

	@GetMapping("/conversation")
	public ResponseEntity<List<Conversation>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@PostMapping("/conversations")
	public ResponseEntity<Conversation> add(@RequestBody ConversationCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
	}
}
