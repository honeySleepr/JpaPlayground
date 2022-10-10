package com.jpaplayground.global.login;

import com.jpaplayground.global.login.dto.OAuthUserInfo;
import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

	private final MemberRepository memberRepository;

	public Member login(OAuthUserInfo userInfo) {
		return memberRepository.save(userInfo.toEntity());
	}
}
