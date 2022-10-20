package com.jpaplayground.global.login;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginUtils {

	public static final String OAUTH_CLIENT_ID = "client_id";
	public static final String OAUTH_STATE = "state";
	public static final String OAUTH_REDIRECT_URI = "redirect_uri";
	public static final String HEADER_ACCESS_TOKEN = "AccessToken";
	public static final String HEADER_REFRESH_TOKEN = "RefreshToken";
	public static final String LOGIN_MEMBER = "loginMember";
	public static final String JWT_ISSUER = "JpaPlayground";

}
