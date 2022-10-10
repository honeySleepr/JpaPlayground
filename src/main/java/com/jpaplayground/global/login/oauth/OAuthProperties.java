package com.jpaplayground.global.login.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuthProperties {

	private final String clientId;
	private final String clientSecret;
	private final String redirectUri;
	private final String accessCodeRequestUrl;
	private final String accessTokenRequestUrl;
	private final String userInfoRequestUrl;
	private final String grantType;

}
