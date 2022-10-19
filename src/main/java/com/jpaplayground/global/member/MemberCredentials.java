package com.jpaplayground.global.member;

import com.jpaplayground.global.login.oauth.OAuthServer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE) // JSON에서 DTO로 변환하기 위해 필요하다(private일 필요는 없다)
@AllArgsConstructor
public class MemberCredentials {

	private Long id;
	private String account;
	private OAuthServer server;
	private String jwtRefreshToken;
}
