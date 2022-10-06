package com.jpaplayground.global.login;

import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.exception.NotFoundException;

public class OAuthServerNotFoundException extends NotFoundException {

	public OAuthServerNotFoundException() {
		super(ErrorCode.OAUTH_SERVER_NOT_FOUND);
	}
}
