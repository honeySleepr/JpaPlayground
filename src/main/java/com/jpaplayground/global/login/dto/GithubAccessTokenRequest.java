package com.jpaplayground.global.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jpaplayground.global.login.OAuthProperties;
import lombok.Getter;

@Getter
public class GithubAccessTokenRequest {

	private final String code;
	@JsonProperty(value = "client_id")
	private final String clientId;
	@JsonProperty(value = "client_secret")
	private final String clientSecret;

	public GithubAccessTokenRequest(String code, OAuthProperties properties) {
		this.code = code;
		this.clientId = properties.getClientId();
		this.clientSecret = properties.getClientSecret();
	}
}
