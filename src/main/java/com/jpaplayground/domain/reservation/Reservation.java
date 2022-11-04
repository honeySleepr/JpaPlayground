package com.jpaplayground.domain.reservation;

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
import org.springframework.data.annotation.CreatedDate;
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
	private LocalDateTime timeToMeet;

	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "buyer_id")
	private Member buyer;

	public Reservation(Member buyer, LocalDateTime timeToMeet) {
		this.buyer = buyer;
		this.timeToMeet = timeToMeet;
	}
}
