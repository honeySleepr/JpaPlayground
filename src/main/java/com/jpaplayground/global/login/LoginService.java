package com.jpaplayground.global.login;

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
	@CacheEvict(key = "#memberId", cacheNames = "member")
	public MemberResponse updateJwtCredentials(Long memberId, String encodedSecretKey, String jwtRefreshToken) {
		Member member = memberRepository.findByIdAndLoggedInTrue(
				memberId) // OSIV가 작동하기 때문에 select query를 날리지 않고 영속성 컨텍스트의 member를 사용한다
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		member.updateJwtCredentials(encodedSecretKey, jwtRefreshToken);
		return new MemberResponse(member);
	}

	@Cacheable(key = "#memberId", cacheNames = "member")
	public MemberCredentials findMemberCredentials(Long memberId) {
		log.debug("====== MemberCredentials Cache Miss");
		MemberCredentials memberCredentials = memberRepository.findMemberCredentialsById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
		return memberCredentials;
	}

	@Transactional
	@CacheEvict(key = "#memberId", cacheNames = "member")
	public MemberResponse logout(Long memberId) {
		Member member = memberRepository.findByIdAndLoggedInTrue(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
		member.logOutAndDeleteJwtCredentials();
		return new MemberResponse(member);
	}
}
