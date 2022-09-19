package com.jpaplayground.global.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

	private String message;
	private int httpStatus;

	public ErrorResponse(ErrorCode errorCode) {
		this.message = errorCode.getMessage();
		this.httpStatus = errorCode.getStatus().value();
	}

}
