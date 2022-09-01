package com.jpaplayground.review;

import com.jpaplayground.product.Product;
import com.jpaplayground.product.ProductRepository;
import com.jpaplayground.review.dto.ReviewCreateRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final ProductRepository productRepository;

	public Review add(ReviewCreateRequest request) {
		Product product = productRepository.findById(request.getProductId())
			.orElseThrow();// Todo : ProductNotFoundException
		return reviewRepository.save(request.toEntity(product));
	}

	public List<Review> findAll() {
		return reviewRepository.findAll();
	}
}
