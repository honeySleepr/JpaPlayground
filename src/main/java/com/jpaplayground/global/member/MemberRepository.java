package com.jpaplayground.global.member;

import com.jpaplayground.global.login.oauth.OAuthServer;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {

	Optional<Member> findByIdAndLoggedInTrue(Long id);

	Optional<Member> findByAccountAndServer(String account, OAuthServer server);

}
