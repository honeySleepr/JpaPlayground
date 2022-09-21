package com.jpaplayground.global;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class MySlice<T> {

	private int size;
	private int page;
	private boolean last;
	private int numberOfElements;
	private List<String> sort;
	private List<T> data;

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
