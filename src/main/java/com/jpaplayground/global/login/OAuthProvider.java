package com.jpaplayground.global.login;

import com.jpaplayground.global.login.dto.OAuthAccessToken;
import com.jpaplayground.global.login.dto.OAuthUserInfo;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface OAuthProvider {

	Pattern serviceNamePattern = Pattern.compile("(.*)OAuthProvider$");

	OAuthUserInfo getUserInfo(OAuthAccessToken accessToken);

	OAuthAccessToken getAccessToken(String code);

	default OAuthServer getOAuthServer() {
		Matcher matcher = serviceNamePattern.matcher(this.getClass().getSimpleName());
		if (matcher.find()) {
			String server = matcher.group(1).toLowerCase();
			return OAuthServer.getOAuthServer(server);
		}
		throw new OAuthServerNotFoundException();
	}
}
