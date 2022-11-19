package com.jpaplayground.domain.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;

//paging 사용할 예정이라 JpaRepository 사용
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
