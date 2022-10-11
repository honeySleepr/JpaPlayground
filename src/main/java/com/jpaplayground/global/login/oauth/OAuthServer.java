package com.jpaplayground.global.login.oauth;

import com.jpaplayground.domain.product.exception.NotFoundException;
import com.jpaplayground.global.exception.ErrorCode;
import java.util.Arrays;

public enum OAuthServer {
	GITHUB,
	KAKAO,
	NAVER;

	private boolean hasName(String name) {
		return this.toString().toLowerCase().equals(name);
	}

	public static OAuthServer getOAuthServer(String server) {
		return Arrays.stream(OAuthServer.values())
			.filter(oAuthServer -> oAuthServer.hasName(server))
			.findFirst()
			.orElseThrow(() -> new NotFoundException(ErrorCode.OAUTH_SERVER_NOT_FOUND));
	}

}
