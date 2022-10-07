package com.jpaplayground.global.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jpaplayground.global.login.OAuthProperties;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NaverAccessTokenRequest {

	private final String code;
	@JsonProperty(value = "client_id")
	private final String clientId;
	@JsonProperty(value = "client_secret")
	private final String clientSecret;
	@JsonProperty(value = "grant_type")
	private final String grantType;

	public NaverAccessTokenRequest(String code, OAuthProperties oAuthProperties) {
		this.grantType = oAuthProperties.getGrantType();
		this.code = code;
		this.clientId = oAuthProperties.getClientId();
		this.clientSecret = oAuthProperties.getClientSecret();
	}

}
