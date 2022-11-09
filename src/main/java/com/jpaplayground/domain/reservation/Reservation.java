package com.jpaplayground.domain.reservation;

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
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
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
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(updatable = false)
	@CreatedDate
	private LocalDateTime createdAt;

	@NotNull
	@LastModifiedDate
	private LocalDateTime lastModifiedAt;

	@NotNull
	private LocalDateTime timeToMeet;

	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "buyer_id")
	private Member buyer;

	@NotNull
	@CreatedBy
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seller_id")
	private Member seller;

	public Reservation(Member buyer, LocalDateTime timeToMeet) {
		this.buyer = buyer;
		this.timeToMeet = timeToMeet;
	}

	public void verifySellerOrBuyer(Long id) {
		if (!buyer.matchesId(id) && !seller.matchesId(id)) {
			throw new ReservationException(ErrorCode.NOT_SELLER_NOR_BUYER);
		}
	}

	public void changeTime(LocalDateTime timeToMeet) {
		this.timeToMeet = timeToMeet;
	}
}
