package com.jpaplayground.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

	ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "Entity not found"),
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "Invalid input value"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
	OAUTH_SERVER_NOT_FOUND(HttpStatus.NOT_FOUND, "지원하는 OAuth 서버가 아닙니다"),
	OAUTH_FAILED(HttpStatus.UNAUTHORIZED, "OAuth 인증이 실패했습니다");

	private final HttpStatus status;
	private final String message;

	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}
