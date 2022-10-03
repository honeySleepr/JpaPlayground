package com.jpaplayground.global.oauth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jpaplayground.global.oauth.OAuthProperties;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GithubAccessTokenRequest {

	private final String code;
	private final String clientId;
	private final String clientSecret;

	public GithubAccessTokenRequest(String code, OAuthProperties properties) {
		this.code = code;
		this.clientId = properties.getClientId();
		this.clientSecret = properties.getClientSecret();
	}
}
