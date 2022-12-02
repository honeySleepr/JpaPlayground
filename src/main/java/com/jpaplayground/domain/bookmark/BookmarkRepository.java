package com.jpaplayground.domain.bookmark;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	@EntityGraph(attributePaths = "product")
	Slice<Bookmark> findAllByMemberId(Long memberId, Pageable pageable);
}
