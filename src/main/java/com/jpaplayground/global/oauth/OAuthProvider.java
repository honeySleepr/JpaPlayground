package com.jpaplayground.global.oauth;

import com.jpaplayground.global.oauth.dto.GitHubAccessToken;
import com.jpaplayground.global.oauth.dto.GitHubUserInfo;
import com.jpaplayground.global.oauth.dto.GithubAccessTokenRequest;
import com.jpaplayground.global.oauth.dto.OAuthUserInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthProvider {

	private final RestTemplate restTemplate;
	private final OAuthPropertyHandler oAuthPropertyHandler;

	public OAuthUserInfo getUserInfo(String code, OAuthServer oAuthServer) {
		OAuthProperties properties = oAuthPropertyHandler.getProperties(oAuthServer);
		GitHubAccessToken accessToken = getAccessToken(code, properties);

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken.getAccessToken());

		HttpEntity<Object> requestHttpEntity = new HttpEntity<>(headers);

		return restTemplate.exchange(properties.getUserInfoRequestUrl(),
			HttpMethod.GET, requestHttpEntity, GitHubUserInfo.class).getBody();
	}

	private GitHubAccessToken getAccessToken(String code, OAuthProperties properties) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));

		HttpEntity<GithubAccessTokenRequest> requestHttpEntity = new HttpEntity<>(
			new GithubAccessTokenRequest(code, properties), headers);

		return restTemplate.postForObject(properties.getAccessTokenRequestUrl()
			, requestHttpEntity, GitHubAccessToken.class);
	}

}
