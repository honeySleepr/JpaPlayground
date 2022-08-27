package com.jpaplayground.product;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository repository;

	public List<Product> findAll() {

		return new ArrayList<>();
	}
}
