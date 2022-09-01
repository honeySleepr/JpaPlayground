package com.jpaplayground.review;

import com.jpaplayground.review.dto.ReviewCreateRequest;
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
public class ReviewController {

	private final ReviewService service;

	@GetMapping("/reviews")
	public ResponseEntity<List<Review>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@PostMapping("/reviews")
	public ResponseEntity<Review> add(@RequestBody ReviewCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
	}
}
