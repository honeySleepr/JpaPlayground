package com.jpaplayground.global.login.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GitHubUserInfo implements OAuthUserInfo {

	@JsonProperty(value = "login")
	private String account;
	private String name;
	private String email;
	@JsonProperty(value = "avatar_url")
	private String profileImageUrl;

}
