package com.jpaplayground.global.response;

import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PagingResponse<T> extends ResponseEntity<PagingResponseDto<T>> {

	public PagingResponse(Slice<T> body) {
		super(new PagingResponseDto<>(body), null, HttpStatus.OK);
	}
}
