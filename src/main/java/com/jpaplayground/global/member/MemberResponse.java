package com.jpaplayground.global.member;

import com.jpaplayground.global.login.oauth.OAuthServer;
import lombok.Getter;

@Getter
public class MemberResponse {

	private final Long id;
	private final String account;
	private final String name;
	private final String email;
	private final String profileImageUrl;
	private final OAuthServer oAuthServer;
	private final Boolean loggedIn;

	public MemberResponse(Member member) {
		this.id = member.getId();
		this.account = member.getAccount();
		this.name = member.getName();
		this.email = member.getEmail();
		this.profileImageUrl = member.getProfileImageUrl();
		this.oAuthServer = member.getServer();
		this.loggedIn = member.getLoggedIn();
	}
}
