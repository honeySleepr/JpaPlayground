package com.jpaplayground.global.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class OAuthAccessToken {

	@JsonProperty(value = "access_token")
	private String accessToken;
	@JsonProperty(value = "token_type")
	private String tokenType;

	public String getTokenHeader() {
		return tokenType + " " + accessToken;
	}

}
