package com.jpaplayground.domain.product.exception;

import com.jpaplayground.global.exception.BusinessException;
import com.jpaplayground.global.exception.ErrorCode;

public class NotFoundException extends BusinessException {

	public NotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
