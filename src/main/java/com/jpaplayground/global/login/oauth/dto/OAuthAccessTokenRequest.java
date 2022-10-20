package com.jpaplayground.global.login.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jpaplayground.global.login.oauth.OAuthProperties;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OAuthAccessTokenRequest {

	private final String code;
	@JsonProperty(value = "client_id")
	private final String clientId;
	@JsonProperty(value = "client_secret")
	private final String clientSecret;
	@JsonProperty(value = "grant_type")
	private final String grantType;

	public OAuthAccessTokenRequest(String code, OAuthProperties oAuthProperties) {
		this.grantType = oAuthProperties.getGrantType();
		this.code = code;
		this.clientId = oAuthProperties.getClientId();
		this.clientSecret = oAuthProperties.getClientSecret();
	}

}
