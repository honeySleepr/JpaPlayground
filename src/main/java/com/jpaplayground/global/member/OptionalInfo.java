package com.jpaplayground.global.member;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OptionalInfo {

	private String name;

	private String email;

	private String profileImageUrl;

	public OptionalInfo(String name, String email, String profileImageUrl) {
		this.name = name;
		this.email = email;
		this.profileImageUrl = profileImageUrl;
	}

	public OptionalInfo(Member member) {
		OptionalInfo optionalInfo = member.getOptionalInfo();
		this.name = optionalInfo.getName();
		this.email = optionalInfo.getEmail();
		this.profileImageUrl = optionalInfo.getProfileImageUrl();
	}

}
