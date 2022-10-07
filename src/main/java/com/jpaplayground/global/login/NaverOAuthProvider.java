package com.jpaplayground.global.login;

import com.jpaplayground.global.login.dto.NaverUserInfo;
import com.jpaplayground.global.login.dto.OAuthAccessToken;
import com.jpaplayground.global.login.dto.OAuthUserInfo;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component("naver")
@Slf4j
public class NaverOAuthProvider implements OAuthProvider {

	public static final String GRANT_TYPE = "grant_type";
	public static final String CLIENT_ID = "client_id";
	public static final String CLIENT_SECRET = "client_secret";
	public static final String CODE = "code";
	private final RestTemplate restTemplate;
	private final OAuthProperties oAuthProperties;

	public NaverOAuthProvider(RestTemplate restTemplate, OAuthPropertyHandler oAuthPropertyHandler) {
		this.restTemplate = restTemplate;
		this.oAuthProperties = oAuthPropertyHandler.getProperties(getOAuthServer());
	}

	@Override
	public OAuthAccessToken getAccessToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		MultiValueMap<String, String> urlParamters = new LinkedMultiValueMap<>();
		urlParamters.add(CODE, code);
		urlParamters.add(CLIENT_ID, oAuthProperties.getClientId());
		urlParamters.add(CLIENT_SECRET, oAuthProperties.getClientSecret());
		urlParamters.add(GRANT_TYPE, oAuthProperties.getGrantType());

		/* TODO: 네이버는 DTO에 담아 보내면 401 에러가 난다. 왜지? */
		HttpEntity<MultiValueMap<String, String>> requestHttpEntity = new HttpEntity<>(urlParamters, headers);

		String url = oAuthProperties.getAccessTokenRequestUrl();
		return restTemplate.postForObject(url, requestHttpEntity, OAuthAccessToken.class);
	}

	@Override
	public OAuthUserInfo getUserInfo(OAuthAccessToken accessToken) {
		log.debug("naver accessToken : {}", accessToken);
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, accessToken.getTokenHeader());

		HttpEntity<Object> requestHttpEntity = new HttpEntity<>(headers);

		String url = oAuthProperties.getUserInfoRequestUrl();
		ResponseMapper responseMapper = restTemplate.exchange(url, HttpMethod.GET, requestHttpEntity,
			ResponseMapper.class).getBody();
		return responseMapper.getResponse();
	}

	/* TODO: Q: Nested Json을 NaverUserInfo 안에서 바로 매핑되도록 처리하려면 어떻게 해야할까..?*/
	static class ResponseMapper {

		private NaverUserInfo response;

		public NaverUserInfo getResponse() {
			return response;
		}
	}
}
