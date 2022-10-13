package com.jpaplayground.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
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
	JWT_ACCESS_TOKEN_MISSING(BAD_REQUEST, "Authorization Header에 AccessToken이 포함되지 않았습니다"),
	JWT_REFRESH_TOKEN_MISSING(BAD_REQUEST,
		"AccessToken 재발급을 위해 RefreshToken이 필요합니다. RefreshToken Header에 RefreshToken이 포함되지 않았습니다"),
	MEMBER_ID_HEADER_MISSING(BAD_REQUEST, "Header에 memberId가 포함되지 않았습니다"),
	INVALID_JWT_ACCESS_TOKEN(UNAUTHORIZED, "AccessToken이 유효하지 않습니다"),
	INVALID_JWT_REFRESH_TOKEN(UNAUTHORIZED, "RefreshToken이 유효하지 않습니다"),
	REFRESH_TOKEN_EXPIRED(UNAUTHORIZED, "RefreshToken이 만료되었습니다"),
	REFRESH_TOKEN_MISMATCH(UNAUTHORIZED, "RefreshToken이 일치하지 않습니다"),
	ACCESS_TOKEN_RENEWED(UNAUTHORIZED, "AccessToken이 재발급되었습니다. 다시 요청해주세요");

	private final HttpStatus status;
	private final String message;

	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}
