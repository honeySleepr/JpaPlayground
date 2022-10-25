package com.jpaplayground.global.login.filter;

import static com.jpaplayground.global.login.LoginUtils.OAUTH_CLIENT_ID;
import static com.jpaplayground.global.login.LoginUtils.OAUTH_REDIRECT_URI;
import static com.jpaplayground.global.login.LoginUtils.OAUTH_STATE;
import com.jpaplayground.global.login.oauth.OAuthProperties;
import com.jpaplayground.global.login.oauth.OAuthPropertyMap;
import com.jpaplayground.global.login.oauth.OAuthServer;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginFilter implements Filter {

	public static final String LOGIN_REGEX = "/login/";
	private final OAuthPropertyMap oAuthPropertyMap;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
		throws IOException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		OAuthServer oAuthServer = OAuthServer.getOAuthServer(parseServer(request));
		OAuthProperties properties = oAuthPropertyMap.getProperties(oAuthServer);
		log.debug("로그인 요청 : {}", oAuthServer);

		String state = UUID.randomUUID().toString();
		request.getSession().setAttribute(OAUTH_STATE, state);

		UriComponents uri = UriComponentsBuilder.fromHttpUrl(properties.getAccessCodeRequestUrl())
			.queryParam(OAUTH_CLIENT_ID, properties.getClientId())
			.queryParam(OAUTH_STATE, state)
			.queryParam(OAUTH_REDIRECT_URI, properties.getRedirectUri())
			.build();

		response.sendRedirect(uri.toString());
	}

	private String parseServer(HttpServletRequest httpRequest) {
		return httpRequest.getRequestURI().replaceFirst(LOGIN_REGEX, Strings.EMPTY);
	}

}
