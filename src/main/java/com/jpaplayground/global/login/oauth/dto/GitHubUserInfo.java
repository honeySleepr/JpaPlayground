package com.jpaplayground.global.login.oauth.dto;

import java.util.Map;

public class GitHubUserInfo extends OAuthUserInfo {

	public GitHubUserInfo(Map<String, Object> oAuthResponse) {
		super(oAuthResponse);

	}

	@Override
	public String getAccount() {
		return (String) oAuthResponse.get("login");
	}

	@Override
	public String getName() {
		return (String) oAuthResponse.get("name");
	}

	@Override
	public String getEmail() {
		return (String) oAuthResponse.get("email");
	}

	@Override
	public String getProfileImageUrl() {
		return (String) oAuthResponse.get("avatar_url");
	}

}
