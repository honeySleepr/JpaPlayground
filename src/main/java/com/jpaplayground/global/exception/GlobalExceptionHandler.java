package com.jpaplayground.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
		log.error("handleBusinessException", e);
		ErrorResponse response = new ErrorResponse(e.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatus()));
	}
}
