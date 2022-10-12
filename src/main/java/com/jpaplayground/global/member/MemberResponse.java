package com.jpaplayground.global.member;

import com.jpaplayground.global.login.oauth.OAuthServer;
import lombok.Getter;

@Getter
public class MemberResponse {

	private Long id;
	private String account;
	private String name;
	private String email;
	private String profileImageUrl;
	private OAuthServer oAuthServer;

	public MemberResponse(Member member) {
		this.id = member.getId();
		this.account = member.getAccount();
		this.name = member.getName();
		this.email = member.getEmail();
		this.profileImageUrl = member.getProfileImageUrl();
		this.oAuthServer = member.getServer();
	}
}
