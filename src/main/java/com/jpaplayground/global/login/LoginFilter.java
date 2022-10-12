package com.jpaplayground.global.login;

import static com.jpaplayground.global.login.LoginUtils.OAUTH_CLIENT_ID;
import static com.jpaplayground.global.login.LoginUtils.OAUTH_REDIRECT_URI;
import static com.jpaplayground.global.login.LoginUtils.OAUTH_STATE;
import com.jpaplayground.global.login.oauth.OAuthProperties;
import com.jpaplayground.global.login.oauth.OAuthPropertyHandler;
import java.io.IOException;
import java.util.UUID;
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
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginFilter implements Filter {

	public static final Pattern pattern = Pattern.compile("/login/(.*)$");
	private final OAuthPropertyHandler oAuthPropertyHandler;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
		throws IOException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String server = parseProvider(request);
		OAuthProperties properties = oAuthPropertyHandler.getProperties(server);
		log.debug("Login request to OAuth server : {}", server);

		String state = UUID.randomUUID().toString();
		request.getSession().setAttribute(OAUTH_STATE, state);

		UriComponents uri = UriComponentsBuilder.fromHttpUrl(properties.getAccessCodeRequestUrl())
			.queryParam(OAUTH_CLIENT_ID, properties.getClientId())
			.queryParam(OAUTH_STATE, state)
			.queryParam(OAUTH_REDIRECT_URI, properties.getRedirectUri())
			.build();

		response.sendRedirect(uri.toString());
	}

	private String parseProvider(HttpServletRequest httpRequest) {
		Matcher matcher = pattern.matcher(httpRequest.getRequestURI());

		if (matcher.find()) {
			return matcher.group(1);
		}
		throw new IllegalArgumentException();
	}

}
