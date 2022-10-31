package com.jpaplayground.global.login;

import static com.jpaplayground.global.login.LoginUtils.HEADER_ACCESS_TOKEN;
import static com.jpaplayground.global.login.LoginUtils.HEADER_REFRESH_TOKEN;
import com.jpaplayground.global.login.jwt.AccessToken;
import com.jpaplayground.global.login.jwt.JwtProvider;
import com.jpaplayground.global.login.jwt.JwtVerifier;
import com.jpaplayground.global.login.oauth.OAuthProperties;
import com.jpaplayground.global.login.oauth.OAuthPropertyMap;
import com.jpaplayground.global.login.oauth.OAuthProvider;
import com.jpaplayground.global.login.oauth.OAuthServer;
import com.jpaplayground.global.login.oauth.dto.OAuthAccessToken;
import com.jpaplayground.global.login.oauth.dto.OAuthUserInfo;
import com.jpaplayground.global.member.MemberResponse;
import com.jpaplayground.global.redis.RedisService;
import com.jpaplayground.global.response.SingleResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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

	private final OAuthProvider oAuthProvider;
	private final OAuthPropertyMap oAuthPropertyMap;
	private final JwtVerifier jwtVerifier;
	private final JwtProvider jwtProvider;
	private final LoginService loginService;
	private final RedisService redisService;

	@GetMapping("login/{server}/callback")
	public SingleResponse<MemberResponse> oAuthLogin(String code, @RequestParam("state") String receivedState,
		@PathVariable String server, @SessionAttribute("state") String sentState) {

		OAuthServer oAuthServer = OAuthServer.getOAuthServer(server);
		OAuthProperties properties = oAuthPropertyMap.getProperties(oAuthServer);
		oAuthProvider.verifyState(receivedState, sentState);

		OAuthAccessToken accessToken = oAuthProvider.getAccessToken(code, properties);
		OAuthUserInfo userInfo = oAuthProvider.getUserInfo(oAuthServer, accessToken, properties);
		log.debug("로그인 유저 : {}", userInfo.getAccount());

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
		return new SingleResponse<>(memberResponse, headers);
	}

	@DeleteMapping("/logout")
	public SingleResponse<MemberResponse> logout(@LoginMemberId Long memberId) {
		redisService.deleteJwtRefreshToken(memberId);
		return new SingleResponse(loginService.logout(memberId));
	}

	@GetMapping("/jwt/refresh")
	public SingleResponse<String> refreshToken(HttpServletRequest request, @AccessToken String accessToken) {

		String refreshToken = request.getHeader(HEADER_REFRESH_TOKEN);

		Claims claims;
		try {
			claims = jwtVerifier.verifyAccessToken(accessToken);
		} catch (ExpiredJwtException e) {
			claims = e.getClaims();
		}
		Long memberId = Long.valueOf(claims.getSubject());

		jwtVerifier.verifyRefreshToken(refreshToken);
		jwtVerifier.verifyMatchingRefreshToken(refreshToken, memberId);
		String newAccessToken = jwtProvider.createAccessToken(memberId);

		HttpHeaders headers = new HttpHeaders();
		headers.set(HEADER_ACCESS_TOKEN, newAccessToken);

		log.debug("AccessToken 재발급 : {}", newAccessToken);
		return new SingleResponse<>("Access Token 재발급 완료", headers);
	}

}
