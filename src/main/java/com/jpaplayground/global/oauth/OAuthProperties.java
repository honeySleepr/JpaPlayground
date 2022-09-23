package com.jpaplayground.global.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuthProperties {

	private final String clientId;
	private final String clientSecret;
	private final String accessCodeRequestUrl;
	private final String redirectUri;

}
