package com.jpaplayground.domain.reservation;

import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.ProductRepository;
import com.jpaplayground.domain.product.exception.ProductException;
import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberRepository;
import com.jpaplayground.global.member.exception.MemberException;
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
	public ReservationResponse create(ReservationCreateRequest request, Long productId, Long sellerId) {
		Product product = productRepository.findByIdAndDeletedFalse(productId)
			.orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

		Member buyer = memberRepository.findById(request.getBuyerId())
			.orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

		if (product.isReserved()) {
			throw new ReservationException(ErrorCode.RESERVED);
		}
		product.verifySeller(sellerId);
		Reservation reservation = reservationRepository.save(new Reservation(buyer, request.getTimeToMeet()));
		product.reserve(reservation);

		return new ReservationResponse(productId, reservation);
	}

	public ReservationResponse findByProductId(Long productId, Long memberId) {
		Product product = productRepository.findByIdAndDeletedFalse(productId)
			.orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

		if (!product.isReserved()) {
			throw new ReservationException(ErrorCode.RESERVATION_NOT_FOUND);
		}
		Reservation reservation = product.getReservation();
		reservation.verifySellerOrBuyer(memberId);
		return new ReservationResponse(productId, reservation);
	}
}
