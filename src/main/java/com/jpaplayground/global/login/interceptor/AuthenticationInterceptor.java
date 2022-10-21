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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class AuthenticationInterceptor implements HandlerInterceptor {

	public static final Pattern bearerPattern = Pattern.compile("^[bB]earer\\s(.*)");
	private final JwtVerifier jwtVerifier;
	private final JwtProvider jwtProvider;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		/* Todo : `POST /products`는 막고 `GET /products`는 열어주려면 여기서 if문 처리하는 방법 뿐인가? */

		log.debug("인터셉터 발동 : {}", request.getRequestURI());

		String accessToken = parseBearerToken(request);
		String refreshToken = request.getHeader(HEADER_REFRESH_TOKEN);
		Claims claims;

		try {
			claims = jwtVerifier.verifyAccessToken(accessToken);
		} catch (ExpiredJwtException e) {
			Long memberId = Long.valueOf(e.getClaims().getSubject());

			String newAccessToken = issueNewAccessToken(refreshToken, memberId);
			response.setHeader(HEADER_ACCESS_TOKEN, newAccessToken);
			log.debug("AccessToken 재발급 : {}", newAccessToken);
			throw new LoginException(ErrorCode.JWT_ACCESS_TOKEN_RENEWED);
		}

		request.setAttribute(LOGIN_MEMBER, Long.valueOf(claims.getSubject()));
		return true;
	}

	private String issueNewAccessToken(String refreshToken, Long memberId) {
		jwtVerifier.verifyRefreshToken(refreshToken);
		jwtVerifier.verifyMatchingRefreshToken(refreshToken, memberId);
		return jwtProvider.createAccessToken(memberId);
	}

	private String parseBearerToken(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header != null) {
			Matcher matcher = bearerPattern.matcher(header);
			if (matcher.find()) {
				return matcher.group(1);
			}
		}
		return null;
	}
}
