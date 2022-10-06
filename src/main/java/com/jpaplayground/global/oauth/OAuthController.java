package com.jpaplayground.global.oauth;

import com.jpaplayground.global.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OAuthController {

	/* TODO: OAuthService 인터페이스로 확장하기 */
	private final GitHubOAuthService gitHubOAuthService;

	@GetMapping("login/github/callback")
	public void login(String code) {

		log.debug("OAuth code received: {}", code);

		Member member = gitHubOAuthService.login(code);
		/* Todo: JWT 토큰 생성, 로그인 사용자 검증(Interceptor), 캐싱(redis? encache?) */
	}
}
