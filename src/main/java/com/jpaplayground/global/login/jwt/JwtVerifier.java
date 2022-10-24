package com.jpaplayground.global.login.jwt;

import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.login.exception.LoginException;
import com.jpaplayground.global.redis.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtVerifier {

	private final SecretKey secretKey;
	private final RedisService redisService;

	public JwtVerifier(JwtProperties jwtProperties, RedisService redisService) {
		this.secretKey = jwtProperties.getSecretKey();
		this.redisService = redisService;
	}

	public Claims verifyAccessToken(String accessToken) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(accessToken)
				.getBody();
		} catch (ExpiredJwtException e) {
			log.debug("AccessToken 기간 만료");
			throw e;
		} catch (Exception e) {
			log.debug("AccessToken 유효하지 않음");
			throw new LoginException(ErrorCode.AUTHORIZATION_FAILED);
		}
	}

	public void verifyRefreshToken(String refreshToken) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(refreshToken);
		} catch (ExpiredJwtException e) {
			log.debug("RefreshToken 기간 만료");
			throw new LoginException(ErrorCode.AUTHORIZATION_FAILED);
		} catch (Exception e) {
			log.debug("RefreshToken 유효하지 않음");
			throw new LoginException(ErrorCode.AUTHORIZATION_FAILED);
		}
	}

	public void verifyMatchingRefreshToken(String receivedRefreshToken, Long memberId) {
		String savedRefreshToken = redisService.getJwtRefreshToken(memberId);
		if (!receivedRefreshToken.equals(savedRefreshToken)) {
			log.debug("RefreshToken 불일치");
			throw new LoginException(ErrorCode.AUTHORIZATION_FAILED);
		}
	}
}
