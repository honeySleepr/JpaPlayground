package com.jpaplayground.global.login.oauth;

import com.jpaplayground.global.exception.BusinessException;
import com.jpaplayground.global.exception.ErrorCode;

public class OAuthFailedException extends BusinessException {

	public OAuthFailedException() {
		super(ErrorCode.OAUTH_FAILED);
	}
}
