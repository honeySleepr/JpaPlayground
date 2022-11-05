package com.jpaplayground.global.login.interceptor;

import com.jpaplayground.global.exception.ErrorCode;
import static com.jpaplayground.global.login.LoginUtils.LOGIN_MEMBER;
import com.jpaplayground.global.login.exception.LoginException;
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

	public static final Map<String, Set<String>> allowedUriAndMethods = Map.of(
		"/products", Set.of(HttpMethod.GET.toString())
	);
	public static final String BEARER_REGEX = "[bB]earer\\s";

	private final JwtVerifier jwtVerifier;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		log.debug("인터셉터 발동 : {}", request.getRequestURI());
		if (isAllowedRequest(request)) {
			log.debug("인터셉터 제외 메서드 : {} {}", request.getMethod(), request.getRequestURI());
			return true;
		}

		String accessToken = parseBearerToken(request);
		Claims claims;

		try {
			claims = jwtVerifier.verifyAccessToken(accessToken);
		} catch (ExpiredJwtException e) {
			throw new LoginException(ErrorCode.JWT_ACCESS_TOKEN_EXPIRED);
		}

		request.setAttribute(LOGIN_MEMBER, Long.valueOf(claims.getSubject()));
		return true;
	}

	private boolean isAllowedRequest(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		String httpMethod = request.getMethod();

		if (allowedUriAndMethods.containsKey(requestURI)) {
			return allowedUriAndMethods.get(requestURI).contains(httpMethod);
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
