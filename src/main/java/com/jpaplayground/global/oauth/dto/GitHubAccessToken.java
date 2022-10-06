package com.jpaplayground.global.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class GitHubAccessToken implements OAuthAccessToken {

	@JsonProperty(value = "access_token")
	private String accessToken;
	private String scope;
	@JsonProperty(value = "token_type")
	private String tokenType;

}
