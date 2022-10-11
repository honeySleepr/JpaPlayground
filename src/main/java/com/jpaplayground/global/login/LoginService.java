package com.jpaplayground.global.login;

import com.jpaplayground.global.login.oauth.OAuthServer;
import com.jpaplayground.global.login.oauth.dto.OAuthUserInfo;
import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberRepository;
import com.jpaplayground.global.member.MemberResponse;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

	private final MemberRepository memberRepository;

	public MemberResponse save(OAuthUserInfo userInfo, String server, SecretKey secretKey, String jwtRefreshToken) {
		Member member = userInfo.toEntity();
		member.setOAuthServer(OAuthServer.getOAuthServer(server));
		member.setJwtCredentials(secretKey, jwtRefreshToken);
		return new MemberResponse(memberRepository.save(member));
	}

}
