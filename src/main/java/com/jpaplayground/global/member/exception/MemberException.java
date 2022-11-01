package com.jpaplayground.global.member.exception;

import com.jpaplayground.global.exception.BusinessException;
import com.jpaplayground.global.exception.ErrorCode;

public class MemberException extends BusinessException {

	public MemberException(ErrorCode errorCode) {
		super(errorCode);
	}
}
