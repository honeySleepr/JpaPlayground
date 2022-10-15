package com.jpaplayground.global.login;

import com.jpaplayground.global.auditing.LoginMember;
import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.exception.NotFoundException;
import com.jpaplayground.global.login.oauth.OAuthServer;
import com.jpaplayground.global.login.oauth.dto.OAuthUserInfo;
import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberCredentials;
import com.jpaplayground.global.member.MemberRepository;
import com.jpaplayground.global.member.MemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
			.map(foundMember -> foundMember.updateInfo(userInfo.toEntity()))
			.orElseGet(() -> {
				log.debug("신규 OAuth 회원가입");
				return userInfo.toEntity();
			});

		member.setServer(oAuthServer);
		return new MemberResponse(memberRepository.save(member));
	}

	@Transactional
	@CacheEvict(key = "#memberId", cacheNames = "jwt")
	public MemberResponse updateJwtCredentials(Long memberId, String encodedSecretKey, String jwtRefreshToken) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		member.updateJwtCredentials(encodedSecretKey, jwtRefreshToken);
		return new MemberResponse(memberRepository.save(member));
	}

	@Cacheable(key = "#memberId", cacheNames = "jwt")
	public MemberCredentials findMemberCredentials(Long memberId) {
		log.debug("====== MemberCredentials Cache Miss");
		return memberRepository.findMemberCredentialsById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	}
}
