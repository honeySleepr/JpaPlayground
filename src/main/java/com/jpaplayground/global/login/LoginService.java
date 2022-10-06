package com.jpaplayground.global.login;

import com.jpaplayground.global.login.dto.OAuthUserInfo;
import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

	private final MemberRepository memberRepository;

	public Member login(OAuthUserInfo userInfo) {
		log.debug("Login user info : {}", userInfo);
		return memberRepository.save(userInfo.toEntity());
	}
}
