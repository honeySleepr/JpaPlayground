package com.jpaplayground.global.login;

import com.jpaplayground.global.exception.ErrorCode;
import static com.jpaplayground.global.login.LoginUtils.HEADER_ACCESS_TOKEN;
import static com.jpaplayground.global.login.LoginUtils.HEADER_REFRESH_TOKEN;
import com.jpaplayground.global.login.exception.LoginException;
import com.jpaplayground.global.login.jwt.JwtProvider;
import com.jpaplayground.global.login.oauth.OAuthProperties;
import com.jpaplayground.global.login.oauth.OAuthPropertyHandler;
import com.jpaplayground.global.login.oauth.OAuthProvider;
import com.jpaplayground.global.login.oauth.dto.OAuthAccessToken;
import com.jpaplayground.global.login.oauth.dto.OAuthUserInfo;
import com.jpaplayground.global.member.MemberResponse;
import com.jpaplayground.global.redis.RedisService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	private final JwtProvider jwtProvider;
	private final LoginService loginService;
	private final RedisService redisService;

	@GetMapping("login/{server}/callback")
	public ResponseEntity<MemberResponse> oAuthLogin(String code, @RequestParam("state") String receivedState,
		@PathVariable String server, @SessionAttribute("state") String sentState) {

		OAuthProvider oAuthProvider = oAuthProviderMap.get(server);
		OAuthProperties properties = oAuthPropertyHandler.getProperties(server);
		if (!oAuthProvider.verifyState(receivedState, sentState)) {
			throw new LoginException(ErrorCode.OAUTH_STATE_MISMATCH);
		}

		OAuthAccessToken accessToken = oAuthProvider.getAccessToken(code, properties);
		OAuthUserInfo userInfo = oAuthProvider.getUserInfo(accessToken, properties);
		log.debug("Login user info : {}", userInfo);

		MemberResponse memberResponse = loginService.save(userInfo, server);
		Long memberId = memberResponse.getId();

		String jwtAccessToken = jwtProvider.createAccessToken(memberId);
		String jwtRefreshToken = jwtProvider.createRefreshToken(memberId);
		log.debug("JWT AccessToken : {}", jwtAccessToken);

		redisService.saveJwtRefreshToken(memberId, jwtRefreshToken);

		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_ACCESS_TOKEN, jwtAccessToken);
		headers.set(HEADER_REFRESH_TOKEN, jwtRefreshToken);

		log.debug("로그인 성공");
		return ResponseEntity.ok().headers(headers).body(memberResponse);
	}

	@DeleteMapping("/logout")
	public ResponseEntity<MemberResponse> logout(@LoginMemberId Long memberId) {
		redisService.deleteJwtRefreshToken(memberId);
		return ResponseEntity.ok(loginService.logout(memberId));
	}
}
