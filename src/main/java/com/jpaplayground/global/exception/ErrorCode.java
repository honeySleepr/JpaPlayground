package com.jpaplayground.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
public enum ErrorCode {

	PRODUCT_NOT_FOUND(NOT_FOUND, "Product not found"),
	MEMBER_NOT_FOUND(NOT_FOUND, "Member not found"),
	INVALID_INPUT_VALUE(BAD_REQUEST, "Invalid input value"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
	OAUTH_SERVER_NOT_FOUND(NOT_FOUND, "지원하는 OAuth 서버가 아닙니다"),
	OAUTH_STATE_MISMATCH(UNAUTHORIZED, "state 값이 일치하지 않습니다"),
	OAUTH_FAILED(UNAUTHORIZED, "OAuth 인증이 실패했습니다"),
	JWT_HEADER_MISSING(BAD_REQUEST, "Header에 JWT가 포함되지 않았습니다"),
	DUPLICATE_MEMBER(FORBIDDEN, "이미 가입된 회원입니다");

	private final HttpStatus status;
	private final String message;

	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}
