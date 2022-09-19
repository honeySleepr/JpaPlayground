package com.jpaplayground.domain.conversation;

import com.jpaplayground.domain.product.Product;
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
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Conversation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	// TODO: 구매자나 판매자 둘 중 한명만 채팅을 지운 경우, 상대방은 채팅을 여전히 볼 수 있도록 구현하기
	private UserType visibleTo;

	@Builder
	private Conversation(String content, Product product) {
		this.content = content;
		this.product = product;
		this.visibleTo = UserType.ALL;
	}

	public static Conversation of(String content, Product product) {
		return Conversation.builder()
			.content(content)
			.product(product)
			.build();
	}

	public void changeVisibilityTo(UserType userType) {
		this.visibleTo = userType;
	}

}
