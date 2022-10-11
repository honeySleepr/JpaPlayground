package com.jpaplayground.global.login.oauth.dto;

import com.jpaplayground.global.member.Member;

public interface OAuthUserInfo {

	String getAccount();

	String getName();

	String getEmail();

	String getProfileImageUrl();

	String toString();

	default Member toEntity() {
		return Member.of(getAccount(), getName(), getEmail(), getProfileImageUrl());
	}
}
