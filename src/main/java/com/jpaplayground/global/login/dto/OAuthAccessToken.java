package com.jpaplayground.global.login.dto;

public interface OAuthAccessToken {

	String getAccessToken();

	String getScope();

	String getTokenType();

}
