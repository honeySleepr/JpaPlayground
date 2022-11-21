package com.jpaplayground.global.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import lombok.Getter;
import org.springframework.validation.BindingResult;

/**
 * <a href="https://github.com/cheese10yun/spring-guide/blob/master/docs/exception-guide.md"> cheese10yun/spring-guide
 * </a> 참고
 */
@Getter
public class ErrorResponse {

	private final String message;
	private final int httpStatus;
	private final List<ErrorDetail> errorDetails;

	public ErrorResponse(ErrorCode errorCode) {
		this.message = errorCode.getMessage();
		this.httpStatus = errorCode.getStatus().value();
		this.errorDetails = new ArrayList<>();
	}

	public ErrorResponse(ErrorCode errorCode, BindingResult bindingResult) {
		this.message = errorCode.getMessage();
		this.httpStatus = errorCode.getStatus().value();
		this.errorDetails = ErrorDetail.of(bindingResult);
	}

	public ErrorResponse(ErrorCode errorCode, Set<ConstraintViolation<?>> constraintViolations) {
		this.message = errorCode.getMessage();
		this.httpStatus = errorCode.getStatus().value();
		this.errorDetails = ErrorDetail.of(constraintViolations);
	}

	@Getter
	private static class ErrorDetail {

		private final String field;
		private final String rejectedValue;
		private final String defaultMessage;

		private ErrorDetail(String field, String rejectedValue, String defaultMessage) {
			this.field = field;
			this.rejectedValue = rejectedValue;
			this.defaultMessage = defaultMessage;
		}

		private static List<ErrorDetail> of(BindingResult bindingResult) {
			return bindingResult.getFieldErrors().stream()
				.map(fieldError -> new ErrorDetail(
					fieldError.getField(),
					fieldError.getRejectedValue() == null ? null : fieldError.getRejectedValue().toString(),
					fieldError.getDefaultMessage())
				)
				.collect(Collectors.toList());
		}

		public static List<ErrorDetail> of(Set<ConstraintViolation<?>> constraintViolations) {
			return constraintViolations.stream()
				.map(violation -> new ErrorDetail(
					violation.getPropertyPath().toString(),
					violation.getInvalidValue() == null ? null : violation.getInvalidValue().toString(),
					violation.getMessage())
				)
				.collect(Collectors.toList());
		}
	}

}
