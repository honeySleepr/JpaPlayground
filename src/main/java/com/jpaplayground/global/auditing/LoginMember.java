package com.jpaplayground.global.auditing;

import com.jpaplayground.global.login.oauth.OAuthServer;
import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberCredentials;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Getter
@ToString
public class LoginMember {

	private Long id;
	private String account;
	private OAuthServer server;

	public void create(MemberCredentials memberCredentials) {
		this.id = memberCredentials.getId();
		this.account = memberCredentials.getAccount();
		this.server = memberCredentials.getServer();
	}

	public Member toEntity() {
		return Member.builder()
			.id(id)
			.build();
	}
}
