package com.jpaplayground.global.login;

import com.jpaplayground.global.login.dto.OAuthAccessToken;
import com.jpaplayground.global.login.dto.OAuthUserInfo;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface OAuthProvider {

	Pattern serviceNamePattern = Pattern.compile("(.*)OAuthProvider$");

	OAuthAccessToken getAccessToken(String code);

	OAuthUserInfo getUserInfo(OAuthAccessToken accessToken);

	default boolean verifyState(String receivedState, String sentState) {
		if (receivedState.equals(sentState)) {
			return true;
		}
		return false;
	}

	default OAuthServer getOAuthServer() {
		Matcher matcher = serviceNamePattern.matcher(this.getClass().getSimpleName());
		if (matcher.find()) {
			String server = matcher.group(1).toLowerCase();
			return OAuthServer.getOAuthServer(server);
		}
		throw new OAuthServerNotFoundException();
	}
}
