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

	/**
	 * `@EntityListeners(AuditingEntityListener.class)`가 붙어있는 Entity가 persist 되면 getCurrentAuditor 메서드도 항상 호출된다. 해당
	 * Entity에 `@CreatedBy`나 `@LastModifiedBy`가 없더라도! 호출된다.
	 * <br> 그래서 memberId가 null이 될 수도 있기 때문에(ex: 로그인 시) null 처리를 해주었다.
	 * <br> 해당 문제는 Member Entity에 BaseEntity를 추가하면서 처음 발생하게 되었다.
	 */
	@Override
	public Optional<Member> getCurrentAuditor() {
		Long memberId = (Long) request.getAttribute(LOGIN_MEMBER);
		log.debug("LoginMemberAuditorAware memberId : {}", memberId);
		if (memberId == null) {
			return Optional.empty();
		}
		return memberRepository.findById(memberId);
	}
}
