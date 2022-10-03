package com.jpaplayground.global.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GitHubAccessToken {

	private String accessToken;
	private String scope;
	private String tokenType;

}
