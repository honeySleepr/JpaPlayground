package com.jpaplayground.global.login.oauth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.login.exception.LoginException;
import com.jpaplayground.global.login.oauth.dto.GitHubUserInfo;
import com.jpaplayground.global.login.oauth.dto.NaverUserInfo;
import com.jpaplayground.global.login.oauth.dto.OAuthAccessToken;
import com.jpaplayground.global.login.oauth.dto.OAuthAccessTokenRequest;
import com.jpaplayground.global.login.oauth.dto.OAuthUserInfo;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthProvider {

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	public OAuthAccessToken getAccessToken(String code, OAuthProperties properties) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.setAll(objectMapper.convertValue(new OAuthAccessTokenRequest(code, properties), new TypeReference<>() {
		}));

		HttpEntity<MultiValueMap<String, String>> requestHttpEntity = new HttpEntity<>(body, headers);

		String url = properties.getAccessTokenRequestUrl();
		return restTemplate.postForObject(url, requestHttpEntity, OAuthAccessToken.class);
	}

	public OAuthUserInfo getUserInfo(OAuthServer oAuthServer, OAuthAccessToken accessToken,
		OAuthProperties properties) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, accessToken.getTokenHeader());

		HttpEntity<Object> requestHttpEntity = new HttpEntity<>(headers);

		Map<String, Object> oAuthResponse = restTemplate.exchange(properties.getUserInfoRequestUrl(), HttpMethod.GET,
			requestHttpEntity, new ParameterizedTypeReference<Map<String, Object>>() {
			}).getBody();

		return OAuthUserInfoFactory.getOAuthUserInfo(oAuthServer, oAuthResponse);
	}

	public void verifyState(String receivedState, String sentState) {
		if (!receivedState.equals(sentState)) {
			log.debug("state 불일치 (CSRF 공격인가!)");
			throw new LoginException(ErrorCode.OAUTH_FAILED);
		}
	}

	private static class OAuthUserInfoFactory {

		public static OAuthUserInfo getOAuthUserInfo(OAuthServer oAuthServer, Map<String, Object> oAuthResponse) {
			switch (oAuthServer) {
				case GITHUB:
					return new GitHubUserInfo(oAuthResponse);
				case NAVER:
					return new NaverUserInfo(oAuthResponse);
				default:
					throw new LoginException(ErrorCode.INVALID_INPUT_VALUE);
			}
		}

	}
}
