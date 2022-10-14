package com.jpaplayground.global.member;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String account;
	private String name;
	private String email;
	private String profileImageUrl;

	@Builder
	private Member(String account, String name, String email, String profileImageUrl) {
		this.account = account;
		this.name = name;
		this.email = email;
		this.profileImageUrl = profileImageUrl;
	}

	public static Member of(String account, String name, String email, String profileImageUrl) {
		return Member.builder()
			.account(account)
			.name(name)
			.email(email)
			.profileImageUrl(profileImageUrl)
			.build();
	}

}
