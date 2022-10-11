package com.jpaplayground.global.login.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jpaplayground.global.login.oauth.OAuthServer;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NaverUserInfo implements OAuthUserInfo {

	@JsonProperty(value = "nickname")
	private String account;
	private String name;
	private String email;
	@JsonProperty(value = "profile_image")
	private String profileImageUrl;
	private OAuthServer oAuthServer;
}
