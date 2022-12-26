package com.jpaplayground.domain.bookmark;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	@EntityGraph(attributePaths = "product")
	Slice<Bookmark> findAllByMemberId(Long memberId, Pageable pageable);

	/*
	@Modifying(clearAutomatically = true)
	@Query("delete from Bookmark b where b.id in :ids")
	int deleteAllByIdInQuery(@Param("ids") List<Long> BookmarkIds);
	*/

	@Transactional
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("delete from Bookmark b where b.product.id = :productId")
	int deleteAllByProductId(@Param("productId") Long productId);

	@EntityGraph(attributePaths = {"product", "product.bookmarks", "member", "member.bookmarks"})
	Optional<Bookmark> findByProductIdAndMemberId(Long productId, Long memberId);
}
