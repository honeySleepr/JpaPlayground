package com.jpaplayground.global.login.jwt;

import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.login.exception.LoginException;
import com.jpaplayground.global.redis.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtVerifier {

	private final SecretKey secretKey;
	private final RedisService redisService;

	public JwtVerifier(JwtProperties jwtProperties, RedisService redisService) {
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecretKey()));
		this.redisService = redisService;
	}

	public void verifyAccessToken(String accessToken) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(accessToken);
		} catch (ExpiredJwtException e) {
			log.debug("JWT AccessToken 기간 만료");
			throw e;
		} catch (JwtException e) {
			throw new LoginException(ErrorCode.JWT_ACCESS_TOKEN_INVALID);
		}
	}

	public void verifyRefreshToken(String refreshToken) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(refreshToken);
		} catch (ExpiredJwtException e) {
			throw new LoginException(ErrorCode.JWT_REFRESH_TOKEN_EXPIRED);
		} catch (JwtException e) {
			throw new LoginException(ErrorCode.JWT_REFRESH_TOKEN_INVALID);
		}
	}

	public void verifyMatchingRefreshToken(String receivedRefreshToken, Long memberId) {
		String savedRefreshToken = redisService.getJwtRefreshToken(memberId);
		if (!receivedRefreshToken.equals(savedRefreshToken)) {
			throw new LoginException(ErrorCode.JWT_REFRESH_TOKEN_MISMATCH);
		}
	}
}
