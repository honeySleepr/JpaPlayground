package com.jpaplayground.global.member;

import com.jpaplayground.global.login.oauth.OAuthServer;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
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
	private String account;
	private String name;
	private String email;
	private String profileImageUrl;
	@Enumerated(EnumType.STRING)
	private OAuthServer oAuthServer;
	private String jwtSecretKey;
	private String jwtRefreshToken;

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

	public void setOAuthServer(OAuthServer oAuthServer) {
		this.oAuthServer = oAuthServer;
	}

	public void setJwtCredentials(SecretKey secretKey, String jwtRefreshToken) {
		this.jwtSecretKey = Encoders.BASE64.encode(secretKey.getEncoded());
		this.jwtRefreshToken = jwtRefreshToken;
	}

	public SecretKey getJwtSecretKey() {
		byte[] decode = Decoders.BASE64.decode(this.jwtSecretKey);
		return Keys.hmacShaKeyFor(decode);
	}
}
