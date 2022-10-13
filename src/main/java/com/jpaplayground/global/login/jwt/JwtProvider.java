package com.jpaplayground.global.login.jwt;

import static com.jpaplayground.global.login.LoginUtils.JWT_ISSUER;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

	public String createAccessToken(Long memberId, SecretKey secretKey) {
		return Jwts.builder()
			.setIssuer(JWT_ISSUER)
			.setSubject(String.valueOf(memberId))
			.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
			.setExpiration(Timestamp.valueOf(LocalDateTime.now().plusMinutes(1L)))
			.signWith(secretKey)
			.compact();
	}

	public String createRefreshToken(Long memberId, SecretKey secretKey) {
		return Jwts.builder()
			.setIssuer(JWT_ISSUER)
			.setSubject(String.valueOf(memberId))
			.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
			.setExpiration(Timestamp.valueOf(LocalDateTime.now().plusWeeks(2L)))
			.signWith(secretKey)
			.compact();
	}

	public SecretKey createSecretKey() {
		return Keys.secretKeyFor(SignatureAlgorithm.HS512);
	}

	public String encodeSecretKey(SecretKey secretKey) {
		return Encoders.BASE64.encode(secretKey.getEncoded());
	}

	public SecretKey decodeSecretKey(String encodedSecretKey) {
		byte[] decode = Decoders.BASE64.decode(encodedSecretKey);
		return Keys.hmacShaKeyFor(decode);
	}
}
