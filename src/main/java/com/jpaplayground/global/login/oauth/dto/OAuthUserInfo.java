package com.jpaplayground.global.login.oauth.dto;

import com.jpaplayground.global.member.Member;
import java.util.Map;

/**
 * <h2><a href="https://github.com/deepIify/oauth-login-be">코드 참고</a></h2>
 */
public abstract class OAuthUserInfo {

	protected Map<String, Object> oAuthResponse;

	// 하위 클래스에서 oAuthResponse를 생성자를 통해 넘기도록 강제함으로써, oAuthResponse가 누락되는 것을 방지할 수 있다.
	protected OAuthUserInfo(Map<String, Object> oAuthResponse) {
		this.oAuthResponse = oAuthResponse;
	}

	public abstract String getAccount();

	public abstract String getName();

	public abstract String getEmail();

	public abstract String getProfileImageUrl();

	public Member toEntity() {
		return Member.of(getAccount(), getName(), getEmail(), getProfileImageUrl());
	}
}
