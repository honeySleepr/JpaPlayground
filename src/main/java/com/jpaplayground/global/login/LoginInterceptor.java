package com.jpaplayground.global.login;

import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.login.exception.LoginException;
import com.jpaplayground.global.login.jwt.JwtProvider;
import com.jpaplayground.global.member.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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

	public static final String BEARER_REGEX = "^[bB]earer\\s.*";
	private final JwtProvider jwtProvider;
	private final LoginService loginService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		log.debug("인터셉터 발동 : {}", request.getRequestURI());
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		log.debug("Authorization Header : {}", token);

		if (token == null || !token.matches(BEARER_REGEX)) {
			throw new LoginException(ErrorCode.JWT_HEADER_MISSING);
		}

		Long id = Long.valueOf(request.getHeader("memberId"));
		Member member = loginService.findById(id);

		try {
			Claims claims = jwtProvider.verifyToken(member.getJwtSecretKey(), token.split("\\s")[1]);
		} catch (ExpiredJwtException e) {

			return false;
		} catch (JwtException e) {
			throw new LoginException(ErrorCode.INVALID_JWT);
		}
		return true;
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
