package com.jpaplayground.conversation;

import com.jpaplayground.common.BaseEntity;
import com.jpaplayground.product.Product;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Conversation extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	@Builder
	private Conversation(String content, Product product) {
		this.content = content;
		this.product = product;
	}

	public static Conversation of(String content, Product product) {
		return Conversation.builder()
			.content(content)
			.product(product)
			.build();
	}

}
