package com.jpaplayground.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

	/* TODO: 도메인 별로 Exception을 만들지 않고 BusinessException 하나로 퉁치고 ErrorCode만으로 구분하는 방법의 단점은? */

	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Product not found"),
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "Invalid input value"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

	private final HttpStatus status;
	private final String message;

	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}
