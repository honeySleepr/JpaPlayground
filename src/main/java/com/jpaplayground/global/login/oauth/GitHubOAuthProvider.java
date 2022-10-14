package com.jpaplayground.global.login.oauth;

import com.jpaplayground.global.login.dto.GitHubUserInfo;
import com.jpaplayground.global.login.dto.GithubAccessTokenRequest;
import com.jpaplayground.global.login.dto.OAuthAccessToken;
import com.jpaplayground.global.login.dto.OAuthUserInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("github")
@RequiredArgsConstructor
public class GitHubOAuthProvider implements OAuthProvider {

	private final RestTemplate restTemplate;

	@Override
	public OAuthAccessToken getAccessToken(String code, OAuthProperties properties) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));

		HttpEntity<GithubAccessTokenRequest> requestHttpEntity = new HttpEntity<>(
			new GithubAccessTokenRequest(code, properties), headers);

		return restTemplate.postForObject(properties.getAccessTokenRequestUrl(), requestHttpEntity,
			OAuthAccessToken.class);
	}

	@Override
	public OAuthUserInfo getUserInfo(OAuthAccessToken accessToken,
		OAuthProperties properties) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, accessToken.getTokenHeader());

		HttpEntity<Object> requestHttpEntity = new HttpEntity<>(headers);

		return restTemplate.exchange(properties.getUserInfoRequestUrl(), HttpMethod.GET, requestHttpEntity,
			GitHubUserInfo.class).getBody();
	}
}
