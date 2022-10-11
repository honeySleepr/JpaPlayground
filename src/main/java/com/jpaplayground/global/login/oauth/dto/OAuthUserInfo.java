package com.jpaplayground.global.login.oauth.dto;

import com.jpaplayground.global.login.oauth.OAuthServer;
import com.jpaplayground.global.member.Member;

public interface OAuthUserInfo {

	String getAccount();

	String getName();

	String getEmail();

	String getProfileImageUrl();

	OAuthServer getOAuthServer();

	String toString();

	default Member toEntity(OAuthServer oAuthServer) {
		return Member.of(getAccount(), getName(), getEmail(), getProfileImageUrl(), oAuthServer);
	}
}
