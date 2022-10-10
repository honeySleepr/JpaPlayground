package com.jpaplayground.global.login.oauth;

import com.jpaplayground.global.exception.BusinessException;
import com.jpaplayground.global.exception.ErrorCode;

public class OAuthServerNotFoundException extends BusinessException {

	public OAuthServerNotFoundException() {
		super(ErrorCode.OAUTH_SERVER_NOT_FOUND);
	}
}
