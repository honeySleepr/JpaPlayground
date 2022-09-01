package com.jpaplayground.review;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceUnitTest {

	@InjectMocks
	ReviewService service;

	@Mock
	ReviewRepository repository;

	/* 추가, 조회 일단은 생략*/

}
