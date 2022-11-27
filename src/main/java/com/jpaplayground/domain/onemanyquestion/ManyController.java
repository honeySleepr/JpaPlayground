package com.jpaplayground.domain.onemanyquestion;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ManyController {

	private final ManyService manyService;

	@PostMapping("/many/one/{oneId}")
	public ResponseEntity<Integer> create(@PathVariable Long oneId) {
		int count = manyService.save(oneId);
		return ResponseEntity.ok(count);
	}
}
