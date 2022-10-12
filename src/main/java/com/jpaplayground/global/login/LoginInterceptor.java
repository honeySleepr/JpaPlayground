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
/*
UnsupportedJwtException – if the claimsJws argument does not represent an Claims JWS
MalformedJwtException – if the claimsJws string is not a valid JWS
SignatureException – if the claimsJws JWS signature validation fails
ExpiredJwtException – if the specified JWT is a Claims JWT and the Claims has an expiration time before the time this method is invoked.
IllegalArgumentException – if the claimsJws string is null or empty or only whitespac
*/
}
