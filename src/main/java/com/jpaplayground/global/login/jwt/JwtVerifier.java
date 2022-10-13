package com.jpaplayground.global.login.jwt;

import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.login.exception.LoginException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtVerifier {

	public void verifyAccessToken(SecretKey secretKey, String accessToken) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(accessToken);
		} catch (ExpiredJwtException e) {
			log.debug("JWT AccessToken 기간 만료");
			throw e;
		} catch (JwtException e) {
			throw new LoginException(ErrorCode.INVALID_JWT_ACCESS_TOKEN);
		}
	}

	public void verifyRefreshToken(SecretKey secretKey, String refreshToken) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(refreshToken);
		} catch (ExpiredJwtException e) {
			throw new LoginException(ErrorCode.REFRESH_TOKEN_EXPIRED);
		} catch (JwtException e) {
			throw new LoginException(ErrorCode.INVALID_JWT_REFRESH_TOKEN);
		}
	}

	public void verifyMatchingRefreshToken(String receivedRefreshToken, String savedRefreshToken) {
		if (!receivedRefreshToken.equals(savedRefreshToken)) {
			throw new LoginException(ErrorCode.REFRESH_TOKEN_MISMATCH);
		}
	}
}
