package com.jpaplayground.domain.product;

import com.jpaplayground.domain.reservation.Reservation;
import com.jpaplayground.global.member.Member;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private Integer price;

	private Boolean deleted;

	@Column(updatable = false)
	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime lastModifiedAt;

	/**
	 * `@OneToMany`(Member->Product) 단방향 관계는 DB 상에는 Many 쪽에 있는 FK를 One 쪽 객체에서 관리하는 구조가 되어버려서 추천하지 않음 그래서 영한님이 추천하신
	 * `@ManyToOne-@OneToMany` 양방향으로 변경
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false, name = "seller_id")
	private Member seller;

	@OneToOne
	@JoinColumn(name = "reservation_id")
	private Reservation reservation;

	/**
	 * `@Builder`를 클래스에 붙이면 모든 필드에 대한 빌더메서드가 만들어지지만, 메서드나 생성자에 붙이면 인자들에 대해서만 빌더 메서드가 만들어진다.
	 */
	@Builder
	private Product(String name, Integer price, Member seller) {
		this.name = name;
		this.price = price;
		this.deleted = false;
		this.seller = seller;
	}

	/**
	 * 일반 정적 팩토리 메서드에서는 여기에서 new를 이용해 생성자를 호출하여 반환한다 (ex: `new Product(name, quantity, price)`) 하지만 이때 생성자에서 같은 타입인 인자의
	 * 순서가 바뀐다면(ex: price<->quantity `Product(String name, Integer price, Integer quantity)`) 입력된 quantity가 price가 되고,
	 * price가 quantity가 되지만 컴파일 에러로 잡아내지 못한다. 그래서 builder를 사용한다. 생성자 시그니처에서 price, quantity 위치가 바뀌어도 price는 price()에,
	 * quantity는 quantity()에 들어간다
	 */
	public static Product of(String name, Integer price) {
		return Product.builder()
			.name(name)
			.price(price)
			.build();
	}

	public void changeDeletedState(boolean tf) {
		this.deleted = tf;
	}

	public void verifySeller(Long sellerId) {
		if (!seller.getId().equals(sellerId)) {
			/* Todo: ProductException */
			throw new IllegalArgumentException();
		}
	}

	public void reserve(Reservation reservation) {
		this.reservation = reservation;
	}
}
