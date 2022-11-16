package com.jpaplayground.domain.reservation;

import com.jpaplayground.global.auditing.BaseTimeEntity;
import com.jpaplayground.global.member.Member;
import java.time.LocalDateTime;
import javax.persistence.Entity;
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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private LocalDateTime timeToMeet;

	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "buyer_id")
	private Member buyer;

	public Reservation(Member buyer, LocalDateTime timeToMeet) {
		this.buyer = buyer;
		this.timeToMeet = timeToMeet;
	}

	public boolean isBuyer(Long memberId) {
		return buyer.matchesId(memberId);
	}

	public void changeTime(LocalDateTime timeToMeet) {
		this.timeToMeet = timeToMeet;
	}
}
