package com.jpaplayground.global.response;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class PagingResponseDto<T> {

	private Paging paging;
	private List<T> data;

	public PagingResponseDto(Slice<T> slice) {
		this.paging = new Paging(slice);
		this.data = slice.getContent();
	}
}
