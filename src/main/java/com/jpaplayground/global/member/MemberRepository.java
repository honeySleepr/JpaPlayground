package com.jpaplayground.global.member;

import com.jpaplayground.global.login.oauth.OAuthServer;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {

	Optional<Member> findByAccountAndServer(String account, OAuthServer server);

	// stream 돌 때 bookmark.matchesProduct()를 사용하기 때문에 product도 미리 fetch join 하였다.
	@EntityGraph(attributePaths = {"bookmarks", "bookmarks.product"})
	Optional<Member> findWithBookmarksById(Long id);
}
