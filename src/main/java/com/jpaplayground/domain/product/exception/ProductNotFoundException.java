package com.jpaplayground.domain.product.exception;

import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.exception.NotFoundException;

public class ProductNotFoundException extends NotFoundException {

	public ProductNotFoundException() {
		super(ErrorCode.ENTITY_NOT_FOUND);
	}
}
