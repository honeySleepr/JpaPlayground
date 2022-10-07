package com.jpaplayground.global.login;

import com.jpaplayground.global.login.dto.GitHubUserInfo;
import com.jpaplayground.global.login.dto.GithubAccessTokenRequest;
import com.jpaplayground.global.login.dto.OAuthAccessToken;
import com.jpaplayground.global.login.dto.OAuthUserInfo;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("github")
@Slf4j
public class GitHubOAuthProvider implements OAuthProvider {

	private final RestTemplate restTemplate;
	private final OAuthProperties oAuthProperties;

	public GitHubOAuthProvider(RestTemplate restTemplate, OAuthPropertyHandler oAuthPropertyHandler) {
		this.restTemplate = restTemplate;
		this.oAuthProperties = oAuthPropertyHandler.getProperties(getOAuthServer());
	}

	@Override
	public OAuthAccessToken getAccessToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));

		HttpEntity<GithubAccessTokenRequest> requestHttpEntity = new HttpEntity<>(
			new GithubAccessTokenRequest(code, oAuthProperties), headers);

		String url = oAuthProperties.getAccessTokenRequestUrl();
		return restTemplate.postForObject(url, requestHttpEntity, OAuthAccessToken.class);
	}

	@Override
	public OAuthUserInfo getUserInfo(OAuthAccessToken accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, accessToken.getTokenHeader());

		HttpEntity<Object> requestHttpEntity = new HttpEntity<>(headers);

		String url = oAuthProperties.getUserInfoRequestUrl();
		return restTemplate.exchange(url, HttpMethod.GET, requestHttpEntity, GitHubUserInfo.class).getBody();
	}
}
