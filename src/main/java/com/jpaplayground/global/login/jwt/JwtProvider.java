package com.jpaplayground.global.login.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

	public static final String ISSUER = "JpaPlayground";

	public String createAccessToken(Long memberId, SecretKey secretKey) {
		return Jwts.builder()
			.setIssuer(ISSUER)
			.setSubject(memberId.toString())
			.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
			.setExpiration(Timestamp.valueOf(LocalDateTime.now().plusMinutes(10L)))
			.signWith(secretKey)
			.compact();
	}

	public String createRefreshToken(Long memberId, SecretKey secretKey) {
		return Jwts.builder()
			.setIssuer(ISSUER)
			.setSubject(memberId.toString())
			.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
			.setExpiration(Timestamp.valueOf(LocalDateTime.now().plusWeeks(2L)))
			.signWith(secretKey)
			.compact();
	}

	public SecretKey createSecretKey() {
		return Keys.secretKeyFor(SignatureAlgorithm.HS512); //or HS384 or HS512
	}
}
/*
1. 의존성 설정
2.
*/
