package com.jpaplayground.global.login;

import com.jpaplayground.global.login.dto.OAuthAccessToken;
import com.jpaplayground.global.login.dto.OAuthUserInfo;
import com.jpaplayground.global.member.Member;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

	private final Map<String, OAuthProvider> oAuthProviderMap;
	private final OAuthPropertyHandler oAuthPropertyHandler;
	private final LoginService loginService;

	@GetMapping("login/{server}/callback")
	public void login(String code, @RequestParam("state") String receivedState, @PathVariable String server,
		@SessionAttribute("state") String sentState) {

		OAuthProvider oAuthProvider = oAuthProviderMap.get(server);
		OAuthProperties properties = oAuthPropertyHandler.getProperties(server);
		if (!oAuthProvider.verifyState(receivedState, sentState)) {
			throw new OAuthFailedException();
		}
		OAuthAccessToken accessToken = oAuthProvider.getAccessToken(code, properties);
		log.debug("OAuth accessToken : {}", accessToken.getTokenHeader());
		OAuthUserInfo userInfo = oAuthProvider.getUserInfo(accessToken, properties);
		log.debug("Login user info : {}", userInfo);

		Member member = loginService.login(userInfo);
		/* Todo: JWT 토큰 생성, 로그인 사용자 검증(Interceptor), 캐싱(redis? encache?) */
	}
}
