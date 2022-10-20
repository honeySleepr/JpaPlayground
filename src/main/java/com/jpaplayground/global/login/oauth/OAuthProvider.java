package com.jpaplayground.global.login.oauth;

import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.login.exception.LoginException;
import com.jpaplayground.global.login.oauth.dto.OAuthAccessToken;
import com.jpaplayground.global.login.oauth.dto.OAuthUserInfo;

public interface OAuthProvider {

	OAuthAccessToken getAccessToken(String code, OAuthProperties properties);

	OAuthUserInfo getUserInfo(OAuthAccessToken accessToken, OAuthProperties properties);

	default void verifyState(String receivedState, String sentState) {
		if (!receivedState.equals(sentState)) {
			/* log */
			throw new LoginException(ErrorCode.OAUTH_FAILED);
		}
	}
}
