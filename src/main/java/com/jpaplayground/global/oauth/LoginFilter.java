package com.jpaplayground.global.oauth;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginFilter implements Filter {

	private static final Pattern pattern = Pattern.compile("/login/(.*)$");
	private final OAuthPropertyHandler oAuthPropertyHandler;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
		throws IOException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		OAuthServer oAuthServer = OAuthServer.getOAuthServer(parseProvider(request));
		log.debug("Login request to OAuth server : {}", oAuthServer.toString());
		OAuthProperties properties = oAuthPropertyHandler.getProperties(oAuthServer);

		response.sendRedirect(properties.getAccessCodeRequestUrl());
	}

	private String parseProvider(HttpServletRequest httpRequest) {
		Matcher matcher = pattern.matcher(httpRequest.getRequestURI());

		if (matcher.find()) {
			return matcher.group(1);
		}
		throw new IllegalArgumentException();
	}

}
