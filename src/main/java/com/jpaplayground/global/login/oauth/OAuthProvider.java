package com.jpaplayground.global.login.oauth;

import com.jpaplayground.global.login.oauth.dto.OAuthAccessToken;
import com.jpaplayground.global.login.oauth.dto.OAuthUserInfo;

public interface OAuthProvider {

	OAuthAccessToken getAccessToken(String code, OAuthProperties properties);

	OAuthUserInfo getUserInfo(OAuthAccessToken accessToken, OAuthProperties properties);

	default boolean verifyState(String receivedState, String sentState) {
		if (receivedState.equals(sentState)) {
			return true;
		}
		return false;
	}
}
