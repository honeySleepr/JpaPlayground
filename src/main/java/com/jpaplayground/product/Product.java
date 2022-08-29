package com.jpaplayground.product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Integer price;

	@Builder /* 필드의 위치가 바뀌어도 정적 팩토리 메서드의 인자 순서를 바꿔줄 필요가 없다 */
	private Product(String name, Integer price) {
		this.name = name;
		this.price = price;
	}

	public static Product of(String name, Integer price) {
		return Product.builder()
			.name(name)
			.price(price)
			.build();
	}
}
