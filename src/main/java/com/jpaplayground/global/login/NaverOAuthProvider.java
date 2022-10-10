package com.jpaplayground.global.login;

import com.jpaplayground.global.login.dto.NaverUserInfo;
import com.jpaplayground.global.login.dto.OAuthAccessToken;
import com.jpaplayground.global.login.dto.OAuthUserInfo;
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

	public static final String GRANT_TYPE = "grant_type";
	public static final String CLIENT_ID = "client_id";
	public static final String CLIENT_SECRET = "client_secret";
	public static final String CODE = "code";
	private final RestTemplate restTemplate;

	@Override
	public OAuthAccessToken getAccessToken(String code, OAuthProperties properties) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		MultiValueMap<String, String> urlParamters = new LinkedMultiValueMap<>();
		urlParamters.add(CODE, code);
		urlParamters.add(CLIENT_ID, properties.getClientId());
		urlParamters.add(CLIENT_SECRET, properties.getClientSecret());
		urlParamters.add(GRANT_TYPE, properties.getGrantType());

		/* TODO: 네이버는 DTO에 담아 보내면 401 에러가 난다. 왜지? */
		//		HttpEntity<NaverAccessTokenRequest> requestHttpEntity = new HttpEntity<>(
		//			new NaverAccessTokenRequest(code, properties), headers);

		HttpEntity<MultiValueMap<String, String>> requestHttpEntity = new HttpEntity<>(urlParamters, headers);

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
