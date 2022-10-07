package com.jpaplayground.global.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jpaplayground.global.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class GitHubUserInfo implements OAuthUserInfo {

	@JsonProperty(value = "login")
	private String account;
	private String name;
	private String email;

	@Override
	public Member toEntity() {
		return Member.of(account, name, email);
	}
}
