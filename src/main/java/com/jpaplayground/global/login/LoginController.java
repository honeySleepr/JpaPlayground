package com.jpaplayground.global.login;

import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.login.exception.LoginException;
import com.jpaplayground.global.login.jwt.JwtProvider;
import com.jpaplayground.global.login.oauth.OAuthProperties;
import com.jpaplayground.global.login.oauth.OAuthPropertyHandler;
import com.jpaplayground.global.login.oauth.OAuthProvider;
import com.jpaplayground.global.login.oauth.dto.OAuthAccessToken;
import com.jpaplayground.global.login.oauth.dto.OAuthUserInfo;
import com.jpaplayground.global.member.MemberResponse;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

	public static final String ACCESS_TOKEN = "accessToken";
	public static final String REFRESH_TOKEN = "refreshToken";
	private final Map<String, OAuthProvider> oAuthProviderMap;
	private final OAuthPropertyHandler oAuthPropertyHandler;
	private final JwtProvider jwtProvider;
	private final LoginService loginService;

	@GetMapping("login/{server}/callback")
	public ResponseEntity<MemberResponse> login(String code, @RequestParam("state") String receivedState,
		@PathVariable String server,
		@SessionAttribute("state") String sentState) {

		OAuthProvider oAuthProvider = oAuthProviderMap.get(server);
		OAuthProperties properties = oAuthPropertyHandler.getProperties(server);
		if (!oAuthProvider.verifyState(receivedState, sentState)) {
			throw new LoginException(ErrorCode.OAUTH_STATE_MISMATCH);
		}

		OAuthAccessToken accessToken = oAuthProvider.getAccessToken(code, properties);
		OAuthUserInfo userInfo = oAuthProvider.getUserInfo(accessToken, properties);
		log.debug("Login user info : {}", userInfo);

		SecretKey secretKey = jwtProvider.createSecretKey();
		String jwtAccessToken = jwtProvider.createAccessToken(userInfo, server, secretKey);
		String jwtRefreshToken = jwtProvider.createRefreshToken(userInfo, server, secretKey);

		MemberResponse memberResponse = loginService.save(userInfo, server, secretKey, jwtRefreshToken);

		HttpHeaders headers = new HttpHeaders();
		headers.set(ACCESS_TOKEN, jwtAccessToken);
		headers.set(REFRESH_TOKEN, jwtRefreshToken);

		return ResponseEntity.ok().headers(headers).body(memberResponse);

		/* Todo: JWT 토큰 생성, 로그인 사용자 검증(Interceptor), 캐싱(redis? encache?) */
	}
}
