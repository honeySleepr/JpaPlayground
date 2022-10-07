package com.jpaplayground.global.oauth;

import java.util.Arrays;

public enum OAuthServer {
	GITHUB("github"),
	KAKAO("kakao"),
	NAVER("naver");

	private final String name;

	OAuthServer(String name) {
		this.name = name;
	}

	private boolean hasName(String name) {
		return this.name.equals(name);
	}

	public static OAuthServer getOAuthServer(String server) {
		return Arrays.stream(OAuthServer.values())
			.filter(oAuthServer -> oAuthServer.hasName(server))
			.findFirst()
			.orElseThrow(OAuthServerNotFoundException::new);
	}

}
