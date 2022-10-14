package com.jpaplayground.global.login;

import com.jpaplayground.global.exception.ErrorCode;
import static com.jpaplayground.global.login.LoginUtils.BEARER_REGEX;
import static com.jpaplayground.global.login.LoginUtils.HEADER_ACCESS_TOKEN;
import static com.jpaplayground.global.login.LoginUtils.HEADER_MEMBER_ID;
import static com.jpaplayground.global.login.LoginUtils.HEADER_REFRESH_TOKEN;
import com.jpaplayground.global.login.exception.LoginException;
import com.jpaplayground.global.login.jwt.JwtProvider;
import com.jpaplayground.global.login.jwt.JwtVerifier;
import com.jpaplayground.global.member.JwtCredentials;
import io.jsonwebtoken.ExpiredJwtException;
import javax.crypto.SecretKey;
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
	private final LoginService loginService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		/* Todo : 특정 요청은 로그인 없이 허용하도록 하려면? (ex:Product 조회) */
		log.debug("인터셉터 발동 : {}", request.getRequestURI());
		verifyHeader(request);
		verifyJwt(request, response);

		loginService.createLoginMember(Long.valueOf(request.getHeader(HEADER_MEMBER_ID)));
		return true;
	}

	/**
	 * AccessToken이 유효하면 API 응답을 내려주고, 만료되었으면 RefreshToken을 검사하여 AccessToken을 재발급해준다
	 */
	private void verifyJwt(HttpServletRequest request, HttpServletResponse response) {
		String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).split("\\s")[1];
		JwtCredentials jwtCredentials = loginService.findJwtCredentials(
			Long.valueOf(request.getHeader(HEADER_MEMBER_ID)));

		SecretKey secretKey = jwtProvider.decodeSecretKey(jwtCredentials.getEncodedSecretKey());

		try {
			jwtVerifier.verifyAccessToken(secretKey, accessToken);
		} catch (ExpiredJwtException e) {
			if (request.getHeader(HEADER_REFRESH_TOKEN) == null) {
				throw new LoginException(ErrorCode.JWT_REFRESH_TOKEN_MISSING);
			}

			String refreshToken = request.getHeader(HEADER_REFRESH_TOKEN);
			jwtVerifier.verifyRefreshToken(secretKey, refreshToken);
			jwtVerifier.verifyMatchingRefreshToken(refreshToken, jwtCredentials.getJwtRefreshToken());

			String newAccessToken = jwtProvider.createAccessToken(Long.valueOf(request.getHeader(HEADER_MEMBER_ID)),
				secretKey);
			response.setHeader(HEADER_ACCESS_TOKEN, newAccessToken);
			log.debug("new AccessToken : {}", newAccessToken);

			throw new LoginException(ErrorCode.ACCESS_TOKEN_RENEWED);
		}
	}

	/**
	 * Authorization Header와 MemberId Header를 검사한다
	 */
	private void verifyHeader(HttpServletRequest request) {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authHeader == null || !authHeader.matches(BEARER_REGEX)) {
			throw new LoginException(ErrorCode.JWT_ACCESS_TOKEN_MISSING);
		}
		if (request.getHeader(HEADER_MEMBER_ID) == null) {
			throw new LoginException(ErrorCode.MEMBER_ID_HEADER_MISSING);
		}
	}
}
