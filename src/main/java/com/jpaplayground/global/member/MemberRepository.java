package com.jpaplayground.global.member;

import com.jpaplayground.global.login.oauth.OAuthServer;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends CrudRepository<Member, Long> {

	Optional<Member> findByIdAndLoggedInTrue(Long id);

	Optional<Member> findByAccountAndServer(String account, OAuthServer server);

	@Query(
		"select new com.jpaplayground.global.member.MemberCredentials(m.id, m.account, m.server, m.encodedSecretKey, m. jwtRefreshToken) " +
		"from Member m " +
		"where m.id =:memberId and m.loggedIn=true")
	Optional<MemberCredentials> findLoginMemberCredentialsById(@Param("memberId") Long memberId);
}
