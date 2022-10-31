package com.jpaplayground.global.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class SingleResponse<T> extends ResponseEntity<SingleResponseDto<T>> {

	public SingleResponse(T data) {
		super(new SingleResponseDto<>(data), null, HttpStatus.OK);
	}

	public SingleResponse(T data, HttpStatus status) {
		super(new SingleResponseDto<>(data), null, status);
	}

	public SingleResponse(T data, MultiValueMap<String, String> headers) {
		super(new SingleResponseDto<>(data), headers, HttpStatus.OK);
	}
}
