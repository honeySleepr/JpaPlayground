package com.jpaplayground.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
public enum ErrorCode {

	PRODUCT_NOT_FOUND(NOT_FOUND, "Product를 찾을 수 없습니다"),
	MEMBER_NOT_FOUND(NOT_FOUND, "Member를 찾을 수 없습니다"),
	INVALID_INPUT_VALUE(BAD_REQUEST, "입력값이 잘못되었습니다"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
	OAUTH_FAILED(UNAUTHORIZED, "OAuth 인증이 실패했습니다"),
	AUTHORIZATION_FAILED(BAD_REQUEST, "로그인 인증이 실패하였습니다"),
	JWT_ACCESS_TOKEN_RENEWED(UNAUTHORIZED, "AccessToken이 재발급되었습니다. 다시 요청해주세요");

	private final HttpStatus status;
	private final String message;

	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}
