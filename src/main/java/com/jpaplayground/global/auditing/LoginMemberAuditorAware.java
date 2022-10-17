package com.jpaplayground.global.auditing;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/* Entity가 생성되거나 수정될 때 마다 이 Bean이 호출된다 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LoginMemberAuditorAware implements AuditorAware<Long> {

	private final LoginMember loginMember;

	@Override
	public Optional<Long> getCurrentAuditor() {
		log.debug("====== LoginMemberAuditorAware 호출");
		return Optional.ofNullable(loginMember.getId());
	}
}
