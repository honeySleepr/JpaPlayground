package com.jpaplayground.global.login;

import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.login.oauth.OAuthServer;
import com.jpaplayground.global.login.oauth.dto.OAuthUserInfo;
import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberRepository;
import com.jpaplayground.global.member.MemberResponse;
import com.jpaplayground.global.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LoginService {

	private final MemberRepository memberRepository;

	@Transactional
	public MemberResponse save(OAuthUserInfo userInfo, String server) {
		OAuthServer oAuthServer = OAuthServer.getOAuthServer(server);

		Member member = memberRepository.findByAccountAndServer(userInfo.getAccount(), oAuthServer)
			.map(existingMember -> existingMember.logInAndUpdateInfo(userInfo.toEntity()))
			.orElseGet(() -> {
				log.debug("신규 OAuth 회원가입");
				Member newMember = userInfo.toEntity();
				newMember.setServer(oAuthServer);
				return memberRepository.save(newMember);
			});

		return new MemberResponse(member);
	}

	@Transactional
	public MemberResponse logout(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
		member.logOut();

		log.debug("로그아웃 성공");
		return new MemberResponse(member);
	}
}
