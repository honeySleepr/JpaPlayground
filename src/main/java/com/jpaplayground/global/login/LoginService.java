package com.jpaplayground.global.login;

import com.jpaplayground.global.login.dto.OAuthUserInfo;
import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberRepository;
import com.jpaplayground.global.member.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

	private final MemberRepository memberRepository;

	public MemberResponse login(OAuthUserInfo userInfo) {
		Member member = memberRepository.save(userInfo.toEntity());

		return new MemberResponse(member);
	}
}
