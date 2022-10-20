package com.jpaplayground.global.auditing;

import static com.jpaplayground.global.login.LoginUtils.LOGIN_MEMBER;
import com.jpaplayground.global.member.Member;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberAuditorAware implements AuditorAware<Member> {

	private final HttpServletRequest request;

	@Override
	public Optional<Member> getCurrentAuditor() {
		Long memberId = (Long) request.getAttribute(LOGIN_MEMBER);
		Member member = Member.builder()
			.id(memberId)
			.build();
		return Optional.ofNullable(member);
	}
}
