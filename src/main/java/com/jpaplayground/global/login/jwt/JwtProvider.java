package com.jpaplayground.global.login.jwt;

import static com.jpaplayground.global.login.LoginUtils.JWT_ISSUER;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.crypto.SecretKey;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtProvider {

	private final SecretKey secretKey;

	public JwtProvider(JwtProperties jwtProperties) {
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecretKey()));
	}

	public String createAccessToken(Long memberId) {
		return Jwts.builder()
			.setIssuer(JWT_ISSUER)
			.setSubject(String.valueOf(memberId))
			.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
			.setExpiration(Timestamp.valueOf(LocalDateTime.now().plusMinutes(10L)))
			.signWith(secretKey)
			.compact();
	}

	public String createRefreshToken(Long memberId) {
		return Jwts.builder()
			.setIssuer(JWT_ISSUER)
			.setSubject(String.valueOf(memberId))
			.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
			.setExpiration(Timestamp.valueOf(LocalDateTime.now().plusWeeks(2L)))
			.signWith(secretKey)
			.compact();
	}
}
