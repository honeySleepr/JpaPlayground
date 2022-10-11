package com.jpaplayground.global.login.oauth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpaplayground.global.login.oauth.dto.NaverAccessTokenRequest;
import com.jpaplayground.global.login.oauth.dto.NaverUserInfo;
import com.jpaplayground.global.login.oauth.dto.OAuthAccessToken;
import com.jpaplayground.global.login.oauth.dto.OAuthUserInfo;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component("naver")
@RequiredArgsConstructor
public class NaverOAuthProvider implements OAuthProvider {

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public OAuthAccessToken getAccessToken(String code, OAuthProperties properties) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));

		// Naver는 MultiValueMap 형태로 보내지 않으면 401 에러가 발생한다
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.setAll(objectMapper.convertValue(new NaverAccessTokenRequest(code, properties), new TypeReference<>() {
		}));

		HttpEntity<MultiValueMap<String, String>> requestHttpEntity = new HttpEntity<>(body, headers);

		String url = properties.getAccessTokenRequestUrl();
		return restTemplate.postForObject(url, requestHttpEntity, OAuthAccessToken.class);
	}

	@Override
	public OAuthUserInfo getUserInfo(OAuthAccessToken accessToken, OAuthProperties properties) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, accessToken.getTokenHeader());

		HttpEntity<Object> requestHttpEntity = new HttpEntity<>(headers);

		String url = properties.getUserInfoRequestUrl();
		ResponseJsonWrapper body = restTemplate.exchange(url, HttpMethod.GET, requestHttpEntity,
			ResponseJsonWrapper.class).getBody();

		return Optional.ofNullable(body).orElseThrow(OAuthFailedException::new).getResponse();
	}

	@Getter
	static class ResponseJsonWrapper {

		/* TODO: Q: Key 값이 response인 Nested Json 안에 NaverUserInfo 값들이 담겨있다. NaverUserInfo 안에서 바로 매핑되도록 처리하려면 어떻게 해야할까..?*/
		private NaverUserInfo response;
	}
}
