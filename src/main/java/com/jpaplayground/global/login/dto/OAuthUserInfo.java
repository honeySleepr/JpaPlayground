package com.jpaplayground.global.login.dto;

import com.jpaplayground.global.member.Member;

public interface OAuthUserInfo {

	String getAccount();

	String getName();

	String getEmail();

	String toString();

	Member toEntity();
}
