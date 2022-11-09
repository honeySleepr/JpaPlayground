package com.jpaplayground.global.exception;

import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	/**
	 * Controller 메서드 인자로 입력되는 DTO의 `@Valid` 검증 실패 시 발생하는 에러를 처리한다
	 */
	@ExceptionHandler(BindException.class)
	public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
		log.error("handleBindException", e);
		ErrorResponse response = new ErrorResponse(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatus()));
	}

	/**
	 * `hibernate.validator.constraints` 또는 `javax.validation.constraints` 패키지의 어노테이션(ex:`@NotNull`)을 Entity의 필드에 사용하였을
	 * 때, 검증 실패 시 발생하는 에러를 처리한다.
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(ConstraintViolationException e) {
		log.error("ConstraintViolationException 처리", e);
		ErrorResponse response = new ErrorResponse(ErrorCode.INVALID_INPUT_VALUE, e.getConstraintViolations());
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatus()));
	}

	/**
	 * 메서드 인자로 입력된 값의 타입이 맞지 않을 경우 발생하는 에러를 처리한다 (ex:@RequestParam Long id에 문자값 읿력)
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
		MethodArgumentTypeMismatchException e) {
		log.error("handleTypeMismatchException", e);
		ErrorResponse response = new ErrorResponse(ErrorCode.INVALID_INPUT_VALUE);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatus()));
	}

	/**
	 * 비즈니스 로직 관련 에러를 처리한다
	 */
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
		log.error("handleBusinessException", e);
		ErrorResponse response = new ErrorResponse(e.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatus()));
	}

	/**
	 * 그 외에 따로 처리해주지 않은 에러는 여기에서 처리해줌으로써 모든 error에 대해 일관된 형태의 API Response로 응답한다
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleAllException(Exception e) {
		log.error("handleAllException", e);
		ErrorResponse response = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatus()));
	}
}
