package com.jpaplayground.global.auditing;

import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberCredentials;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class LoginMember {

	private Long id;
	//	private String account;
	//	private String name;
	//	private String email;
	//	private String profileImageUrl;
	//	private OAuthServer server;

	public void create(MemberCredentials memberCredentials) {
		this.id = memberCredentials.getId();
		//		this.account = memberCredentials.getAccount();
		//		this.name = memberCredentials.getName();
		//		this.email = memberCredentials.getEmail();
		//		this.profileImageUrl = memberCredentials.getProfileImageUrl();
		//		this.server = memberCredentials.getServer();
	}

	public Member toEntity() {
		return Member.builder()
			.id(id)
			//			.account(account)
			//			.name(name)
			//			.email(email)
			//			.profileImageUrl(profileImageUrl)
			//			.server(server)
			.build();
	}
}
