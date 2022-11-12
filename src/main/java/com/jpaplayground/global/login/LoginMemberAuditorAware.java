package com.jpaplayground.global.login;

import static com.jpaplayground.global.login.LoginUtils.LOGIN_MEMBER;
import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberRepository;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginMemberAuditorAware implements AuditorAware<Member> {

	private final HttpServletRequest request;
	private final MemberRepository memberRepository;

	@Override
	public Optional<Member> getCurrentAuditor() {
		Long memberId = (Long) request.getAttribute(LOGIN_MEMBER);
		log.debug("LoginMemberAuditorAware memberId : {}", memberId);
		return memberRepository.findById(memberId);
	}
}
