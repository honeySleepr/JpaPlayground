package com.jpaplayground.global.member;

import lombok.Getter;

@Getter
public class MemberResponse {

	private String account;
	private String name;
	private String email;
	private String profileImageUrl;

	public MemberResponse(Member member) {
		this.account = member.getAccount();
		this.name = member.getName();
		this.email = member.getEmail();
		this.profileImageUrl = member.getProfileImageUrl();
	}
}
