package com.jpaplayground.domain.product.exception;

import com.jpaplayground.global.exception.BusinessException;
import com.jpaplayground.global.exception.ErrorCode;

public class ProductException extends BusinessException {

	public ProductException(ErrorCode errorCode) {
		super(errorCode);
	}
}
