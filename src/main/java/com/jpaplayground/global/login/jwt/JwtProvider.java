package com.jpaplayground.global.login.jwt;

import com.jpaplayground.global.login.oauth.dto.OAuthUserInfo;
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

	public String createAccessToken(OAuthUserInfo userInfo, String server, SecretKey secretKey) {
		return Jwts.builder()
			.setIssuer(ISSUER)
			.setSubject(server.toUpperCase() + " " + userInfo.getAccount())
			.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
			.setExpiration(Timestamp.valueOf(LocalDateTime.now().plusMinutes(10L)))
			.signWith(secretKey)
			.compact();
	}

	public String createRefreshToken(OAuthUserInfo userInfo, String server, SecretKey secretKey) {
		return Jwts.builder()
			.setIssuer(ISSUER)
			.setSubject(server.toUpperCase() + " " + userInfo.getAccount())
			.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
			.setExpiration(Timestamp.valueOf(LocalDateTime.now().plusWeeks(2L)))
			.signWith(secretKey)
			.compact();
	}

	public SecretKey createSecretKey() {
		return Keys.secretKeyFor(SignatureAlgorithm.HS512);
	}
}