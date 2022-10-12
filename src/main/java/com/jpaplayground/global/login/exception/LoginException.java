package com.jpaplayground.global.login.exception;

import com.jpaplayground.global.exception.BusinessException;
import com.jpaplayground.global.exception.ErrorCode;

public class LoginException extends BusinessException {

	public LoginException(ErrorCode errorCode) {
		super(errorCode);
	}
}
