package com.jpaplayground.global.auditing;

import com.jpaplayground.global.login.oauth.OAuthServer;
import com.jpaplayground.global.member.Member;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class LoginMember {

	private Long id;
	private String account;
	private String name;
	private String email;
	private String profileImageUrl;
	private OAuthServer server;

	public void create(Member member) {
		this.id = member.getId();
		this.account = member.getAccount();
		this.name = member.getName();
		this.email = member.getEmail();
		this.profileImageUrl = member.getProfileImageUrl();
		this.server = member.getServer();
	}

	public Member toEntity() {
		return Member.builder()
			.id(id)
			.account(account)
			.name(name)
			.email(email)
			.profileImageUrl(profileImageUrl)
			.server(server)
			.build();
	}
}
