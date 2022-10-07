package com.jpaplayground.domain.product.exception;

import com.jpaplayground.global.exception.BusinessException;
import com.jpaplayground.global.exception.ErrorCode;

public class ProductNotFoundException extends BusinessException {

	public ProductNotFoundException() {
		super(ErrorCode.ENTITY_NOT_FOUND);
	}
}
