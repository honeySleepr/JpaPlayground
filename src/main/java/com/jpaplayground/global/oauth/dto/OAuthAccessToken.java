package com.jpaplayground.global.oauth.dto;

public interface OAuthAccessToken {

	String getAccessToken();

	String getScope();

	String getTokenType();

}
