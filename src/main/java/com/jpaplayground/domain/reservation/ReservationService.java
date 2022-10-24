package com.jpaplayground.domain.reservation;

import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.ProductRepository;
import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.exception.NotFoundException;
import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;

	@Transactional
	public ReservationResponse create(ReservationCreateRequest request, Long sellerId) {
		Product product = productRepository.findById(request.getProductId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

		Member buyer = memberRepository.findById(request.getBuyerId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		product.verifySeller(sellerId);
		Reservation reservation = reservationRepository.save(new Reservation(buyer, request.getTimeToMeet()));
		product.reserve(reservation);

		return new ReservationResponse(reservation);
	}
}
