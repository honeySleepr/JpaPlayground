package com.jpaplayground.global.oauth;

import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.oauth.dto.OAuthUserInfo;

public interface OAuthService {

	public Member login(OAuthUserInfo userInfo);

}
