package com.jpaplayground.global.member;

import com.jpaplayground.global.login.oauth.OAuthServer;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
	@Column(updatable = false)
	private String account;
	private String name;
	private String email;
	private String profileImageUrl;
	@Column(updatable = false)
	@Enumerated(EnumType.STRING)
	private OAuthServer server;
	private String jwtRefreshToken;
	private Boolean loggedIn;

	@Builder
	private Member(Long id, String account, String name, String email, String profileImageUrl, OAuthServer server) {
		this.id = id;
		this.account = account;
		this.name = name;
		this.email = email;
		this.profileImageUrl = profileImageUrl;
		this.server = server;
		this.loggedIn = true;
	}

	public static Member of(String account, String name, String email, String profileImageUrl) {
		return Member.builder()
			.account(account)
			.name(name)
			.email(email)
			.profileImageUrl(profileImageUrl)
			.build();
	}

	public void setServer(OAuthServer server) {
		this.server = server;
	}

	public void updateJwtCredentials(String jwtRefreshToken) {
		this.jwtRefreshToken = jwtRefreshToken;
	}

	public Member logInAndUpdateInfo(Member member) {
		this.name = member.getName();
		this.email = member.getEmail();
		this.profileImageUrl = member.getProfileImageUrl();
		this.loggedIn = true;
		return this;
	}

	public void logOutAndDeleteJwtCredentials() {
		this.jwtRefreshToken = null;
		this.loggedIn = false;
	}
}
