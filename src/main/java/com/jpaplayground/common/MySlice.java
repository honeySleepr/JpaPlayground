package com.jpaplayground.common;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class MySlice<T> {

	int size;
	int page;
	boolean last;
	int numberOfElements;
	List<String> sort;
	List<T> data;

	public MySlice(Slice<T> slice) {
		this.size = slice.getSize();
		this.page = slice.getNumber();
		this.numberOfElements = slice.getNumberOfElements();
		this.sort = slice.getSort().stream()
			.map(order -> order.getProperty() + " : " + order.getDirection())
			.collect(Collectors.toList());
		this.last = slice.isLast();
		this.data = slice.getContent();
	}
}
