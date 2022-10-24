package com.jpaplayground.global.response;

import lombok.Getter;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PagingResponse<T> extends ResponseEntity<PagingResponseDto<T>> {

	public PagingResponse(Slice<T> body) {
		super(new PagingResponseDto<>(body), null, HttpStatus.OK);
	}
}
