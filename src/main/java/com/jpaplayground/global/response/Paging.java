package com.jpaplayground.global.response;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class Paging {

	private int size;
	private int page;
	private boolean last;
	private int numberOfElements;
	private List<String> sort;

	public Paging(Slice slice) {
		this.size = slice.getSize();
		this.page = slice.getNumber();
		this.numberOfElements = slice.getNumberOfElements();
		this.last = slice.isLast();
		this.sort = slice.getSort().stream()
			.map(order -> order.getProperty() + " : " + order.getDirection())
			.collect(Collectors.toList());
	}
}
