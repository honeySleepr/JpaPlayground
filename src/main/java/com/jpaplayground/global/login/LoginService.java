package com.jpaplayground.global.login;

import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.exception.NotFoundException;
import com.jpaplayground.global.login.oauth.OAuthServer;
import com.jpaplayground.global.login.oauth.dto.OAuthUserInfo;
import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberRepository;
import com.jpaplayground.global.member.MemberResponse;
import io.jsonwebtoken.io.Encoders;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

	private final MemberRepository memberRepository;

	public MemberResponse save(OAuthUserInfo userInfo, String server, SecretKey secretKey, String jwtRefreshToken) {
		OAuthServer oAuthServer = OAuthServer.getOAuthServer(server);
		Member member = memberRepository.findByAccountAndServer(oAuthServer, userInfo.getAccount())
			.map(foundMember -> foundMember.updateInfo(userInfo.toEntity()))
			.orElseGet(userInfo::toEntity);

		member.setServer(oAuthServer);
		member.setJwtCredentials(Encoders.BASE64.encode(secretKey.getEncoded()), jwtRefreshToken);
		return new MemberResponse(memberRepository.save(member));
	}

	public Member findById(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	}

}
