package com.jpaplayground.global.login.oauth;

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
			.orElseThrow(OAuthServerNotFoundException::new);
	}

}
