package com.jpaplayground.domain.product;

import com.jpaplayground.domain.product.exception.ProductException;
import com.jpaplayground.domain.reservation.Reservation;
import com.jpaplayground.domain.reservation.exception.ReservationException;
import com.jpaplayground.global.exception.ErrorCode;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
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

	@NotBlank
	private String name;

	@NotNull
	@Positive
	private Integer price;

	@NotNull
	private Boolean deleted;

	@NotNull
	@Column(updatable = false)
	@CreatedDate
	private LocalDateTime createdAt;

	@NotNull
	@LastModifiedDate
	private LocalDateTime lastModifiedAt;

	/**
	 * `@OneToMany`(Member->Product) 단방향 관계는 DB 상에는 Many 쪽에 있는 FK를 One 쪽 객체에서 관리하는 구조가 되어버려서 추천하지 않음 그래서 영한님이 추천하신
	 * `@ManyToOne-@OneToMany` 양방향으로 변경
	 */
	@NotNull
	@CreatedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false, name = "seller_id")
	private Member seller;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_id")
	private Reservation reservation;

	private Product(String name, Integer price) {
		this.name = name;
		this.price = price;
		this.deleted = false;
	}

	public static Product of(String name, Integer price) {
		return new Product(name, price);
	}

	public void changeDeletedState(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isSeller(Long memberId) {
		return seller.matchesId(memberId);
	}

	public void verifySeller(Long memberId) {
		if (!seller.matchesId(memberId)) {
			throw new ProductException(ErrorCode.NOT_SELLER);
		}
	}

	public void reserve(Reservation reservation) {
		this.reservation = reservation;
	}

	public boolean isReserved() {
		return this.reservation != null;
	}

	public void update(String name, Integer price) {
		if (name != null) {
			this.name = name;
		}
		if (price != null) {
			this.price = price;
		}
	}

	public void verifyReservationDoesNotExist() {
		if (reservation != null) {
			throw new ReservationException(ErrorCode.RESERVED);
		}
	}

	public void verifyReservationExists() {
		if (reservation == null) {
			throw new ReservationException(ErrorCode.RESERVATION_NOT_FOUND);
		}
	}

	public void verifySellerOrBuyer(Long memberId) {
		if (!seller.matchesId(memberId) && !reservation.isBuyer(memberId)) {
			throw new ReservationException(ErrorCode.NOT_SELLER_NOR_BUYER);
		}
	}
}
