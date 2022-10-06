package com.jpaplayground.global.oauth;

import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberRepository;
import com.jpaplayground.global.oauth.dto.GitHubAccessToken;
import com.jpaplayground.global.oauth.dto.GitHubUserInfo;
import com.jpaplayground.global.oauth.dto.GithubAccessTokenRequest;
import com.jpaplayground.global.oauth.dto.OAuthUserInfo;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GitHubOAuthService {

	private static final Pattern serviceNamePattern = Pattern.compile("(.*)OAuthService$");
	private final RestTemplate restTemplate;
	private final MemberRepository memberRepository;
	private final OAuthProperties oAuthProperties;

	@Autowired
	public GitHubOAuthService(OAuthPropertyHandler oAuthPropertyHandler, RestTemplate restTemplate,
		MemberRepository memberRepository) {
		this.restTemplate = restTemplate;
		this.memberRepository = memberRepository;
		this.oAuthProperties = oAuthPropertyHandler.getProperties(getOAuthServer());
	}

	public Member login(String code) {
		GitHubAccessToken accessToken = getAccessToken(code);
		log.debug("github access token : {}", accessToken.getAccessToken());
		OAuthUserInfo userInfo = getUserInfo(accessToken);
		log.debug("github user info : {}", userInfo);

		return memberRepository.save(userInfo.toEntity());
	}

	private GitHubAccessToken getAccessToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));

		HttpEntity<GithubAccessTokenRequest> requestHttpEntity = new HttpEntity<>(
			new GithubAccessTokenRequest(code, oAuthProperties), headers);

		return restTemplate.postForObject(oAuthProperties.getAccessTokenRequestUrl()
			, requestHttpEntity, GitHubAccessToken.class);
	}

	private OAuthUserInfo getUserInfo(GitHubAccessToken accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken.getAccessToken());

		HttpEntity<Object> requestHttpEntity = new HttpEntity<>(headers);

		return restTemplate.exchange(oAuthProperties.getUserInfoRequestUrl(),
			HttpMethod.GET, requestHttpEntity, GitHubUserInfo.class).getBody();
	}

	/* TODO : 인터페이스에서 default 메서드로 만들기 */
	private OAuthServer getOAuthServer() {
		Matcher matcher = serviceNamePattern.matcher(this.getClass().getSimpleName());
		if (matcher.find()) {
			String server = matcher.group(1).toLowerCase();
			return OAuthServer.getOAuthServer(server);
		}
		throw new OAuthServerNotFoundException();
	}
}
