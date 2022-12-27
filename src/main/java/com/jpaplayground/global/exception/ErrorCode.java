package com.jpaplayground.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
public enum ErrorCode {

	// Global
	INVALID_INPUT_VALUE(BAD_REQUEST, "입력값이 잘못되었습니다"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),

	// Product
	PRODUCT_NOT_FOUND(NOT_FOUND, "Product를 찾을 수 없습니다"),
	NOT_SELLER(FORBIDDEN, "해당 제품의 판매자가 아닙니다"),
	PRODUCT_SOLD(BAD_REQUEST, "이미 판매된 제품입니다"),

	// Member
	MEMBER_NOT_FOUND(NOT_FOUND, "Member를 찾을 수 없습니다"),

	// Authentication
	OAUTH_FAILED(FORBIDDEN, "OAuth 인증이 실패했습니다"),
	AUTHORIZATION_FAILED(FORBIDDEN, "로그인 인증이 실패하였습니다"),
	JWT_ACCESS_TOKEN_EXPIRED(FORBIDDEN, "AccessToken이 만료되었습니다"),

	// Reservation
	RESERVED(BAD_REQUEST, "이미 예약된 제품입니다"),
	RESERVATION_NOT_FOUND(NOT_FOUND, "예약 되지 않은 제품입니다"),
	NOT_SELLER_NOR_BUYER(FORBIDDEN, "판매자 또는 구매자만 예약을 조회할 수 있습니다");

	private final HttpStatus status;
	private final String message;

	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}
