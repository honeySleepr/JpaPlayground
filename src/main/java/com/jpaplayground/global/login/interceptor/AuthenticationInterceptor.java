package com.jpaplayground.global.login.interceptor;

import com.jpaplayground.global.exception.ErrorCode;
import static com.jpaplayground.global.login.LoginUtils.HEADER_ACCESS_TOKEN;
import static com.jpaplayground.global.login.LoginUtils.HEADER_REFRESH_TOKEN;
import static com.jpaplayground.global.login.LoginUtils.LOGIN_MEMBER;
import com.jpaplayground.global.login.exception.LoginException;
import com.jpaplayground.global.login.jwt.JwtProvider;
import com.jpaplayground.global.login.jwt.JwtVerifier;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

	public static final String BEARER_REGEX = "[bB]earer\\s";

	private static final Map<String, Set<String>> excludedRequests = Map.of(
		"/products", Set.of(HttpMethod.GET.toString())
	);

	private final JwtVerifier jwtVerifier;
	private final JwtProvider jwtProvider;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (isExcludedRequest(request)) {
			return true;
		}
		log.debug("인터셉터 발동 : {}", request.getRequestURI());

		String accessToken = parseBearerToken(request);
		String refreshToken = request.getHeader(HEADER_REFRESH_TOKEN);
		Claims claims;

		try {
			claims = jwtVerifier.verifyAccessToken(accessToken);
		} catch (ExpiredJwtException e) {
			Long memberId = Long.valueOf(e.getClaims().getSubject());

			jwtVerifier.verifyRefreshToken(refreshToken);
			jwtVerifier.verifyMatchingRefreshToken(refreshToken, memberId);
			String newAccessToken = jwtProvider.createAccessToken(memberId);

			response.setHeader(HEADER_ACCESS_TOKEN, newAccessToken);
			log.debug("AccessToken 재발급 : {}", newAccessToken);
			throw new LoginException(ErrorCode.JWT_ACCESS_TOKEN_RENEWED);
		}

		request.setAttribute(LOGIN_MEMBER, Long.valueOf(claims.getSubject()));
		return true;
	}

	private boolean isExcludedRequest(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		String httpMethod = request.getMethod();

		if (excludedRequests.containsKey(requestURI)) {
			return excludedRequests.get(requestURI).contains(httpMethod);
		}
		return false;
	}

	private String parseBearerToken(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header == null) {
			return null;
		}
		return header.replaceFirst(BEARER_REGEX, Strings.EMPTY);
	}
}
