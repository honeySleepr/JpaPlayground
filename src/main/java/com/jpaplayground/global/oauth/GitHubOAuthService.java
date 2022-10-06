package com.jpaplayground.global.oauth;

import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberRepository;
import com.jpaplayground.global.oauth.dto.OAuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GitHubOAuthService implements OAuthService {

	private final MemberRepository memberRepository;

	@Override
	public Member login(OAuthUserInfo userInfo) {
		log.debug("github user info : {}", userInfo);
		return memberRepository.save(userInfo.toEntity());
	}
}
