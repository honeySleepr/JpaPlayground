package com.jpaplayground.global.login;

import com.jpaplayground.global.auditing.LoginMember;
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
import java.util.Map;
import javax.crypto.SecretKey;
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
	private final LoginMember loginMember;

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
		Long memberId = loginService.save(userInfo, server).getId();

		SecretKey secretKey = jwtProvider.createSecretKey();
		String jwtAccessToken = jwtProvider.createAccessToken(memberId, secretKey);
		String jwtRefreshToken = jwtProvider.createRefreshToken(memberId, secretKey);
		log.debug("JWT AccessToken : {}", jwtAccessToken);

		String encodedSecretKey = jwtProvider.encodeSecretKey(secretKey);
		MemberResponse memberResponse = loginService.updateJwtCredentials(memberId, encodedSecretKey, jwtRefreshToken);

		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_ACCESS_TOKEN, jwtAccessToken);
		headers.set(HEADER_REFRESH_TOKEN, jwtRefreshToken);

		return ResponseEntity.ok().headers(headers).body(memberResponse);
	}

	@DeleteMapping("/logout")
	public ResponseEntity<MemberResponse> logout() {
		log.debug("Logout Member : {}", loginMember);
		return ResponseEntity.ok(loginService.logout(loginMember.getId()));
	}
}
