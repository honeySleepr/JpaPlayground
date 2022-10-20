package com.jpaplayground.global.login;

import com.jpaplayground.global.exception.ErrorCode;
import static com.jpaplayground.global.login.LoginUtils.BEARER_REGEX;
import static com.jpaplayground.global.login.LoginUtils.HEADER_ACCESS_TOKEN;
import static com.jpaplayground.global.login.LoginUtils.HEADER_REFRESH_TOKEN;
import com.jpaplayground.global.login.exception.LoginException;
import com.jpaplayground.global.login.jwt.JwtProvider;
import com.jpaplayground.global.login.jwt.JwtVerifier;
import io.jsonwebtoken.ExpiredJwtException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

	private final JwtVerifier jwtVerifier;
	private final JwtProvider jwtProvider;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		/* Todo : `POST /products`는 막고 `GET /products`는 열어주려면 여기서 if문 처리하는 방법 뿐인가? */

		log.debug("인터셉터 발동 : {}", request.getRequestURI());
		checkAuthorizationHeader(request);

		verifyJwt(request, response);

		/* Todo : Argument Resolver */
		return true;
	}

	/**
	 * AccessToken이 유효하면 API 응답을 내려주고, 만료되었으면 RefreshToken을 검사하여 AccessToken을 재발급해준다
	 */
	private void verifyJwt(HttpServletRequest request, HttpServletResponse response) {
		String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).split("\\s")[1];

		try {
			jwtVerifier.verifyAccessToken(accessToken);
		} catch (ExpiredJwtException e) {
			checkRequestTokenHeader(request);

			Long memberId = Long.valueOf(e.getClaims().getSubject());
			String refreshToken = request.getHeader(HEADER_REFRESH_TOKEN);
			jwtVerifier.verifyRefreshToken(refreshToken);
			jwtVerifier.verifyMatchingRefreshToken(refreshToken, memberId);

			String newAccessToken = jwtProvider.createAccessToken(memberId);
			response.setHeader(HEADER_ACCESS_TOKEN, newAccessToken);
			log.debug("new AccessToken : {}", newAccessToken);

			throw new LoginException(ErrorCode.JWT_ACCESS_TOKEN_RENEWED);
		}
	}

	private void checkRequestTokenHeader(HttpServletRequest request) {
		if (request.getHeader(HEADER_REFRESH_TOKEN) == null) {
			/* 에러는 두루뭉실하게 하되 log을 자세하게 찍자 */
			throw new LoginException(ErrorCode.JWT_REFRESH_TOKEN_MISSING);
		}
	}

	private void checkAuthorizationHeader(HttpServletRequest request) {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authHeader == null || !authHeader.matches(BEARER_REGEX)) {
			throw new LoginException(ErrorCode.JWT_ACCESS_TOKEN_MISSING);
		}
	}
}
