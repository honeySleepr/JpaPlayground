package com.jpaplayground.domain.reservation;

import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.ProductRepository;
import com.jpaplayground.domain.product.exception.ProductException;
import com.jpaplayground.domain.reservation.dto.ReservationCreateRequest;
import com.jpaplayground.domain.reservation.dto.ReservationResponse;
import com.jpaplayground.domain.reservation.dto.ReservationUpdateRequest;
import com.jpaplayground.domain.reservation.exception.ReservationException;
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
	public ReservationResponse create(ReservationCreateRequest request, Long productId, Long memberId) {
		Product product = productRepository.findByIdAndDeletedFalse(productId)
			.orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

		Member buyer = memberRepository.findById(request.getBuyerId())
			.orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

		if (product.isReserved()) {
			throw new ReservationException(ErrorCode.RESERVED);
		}
		product.verifySeller(memberId);
		Reservation reservation = reservationRepository.save(new Reservation(buyer, request.getTimeToMeet()));
		product.reserve(reservation);

		return new ReservationResponse(product, reservation);
	}

	@Transactional(readOnly = true)
	public ReservationResponse findByProductId(Long productId, Long memberId) {
		Product product = productRepository.findByIdAndDeletedFalse(productId)
			.orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

		Reservation reservation = product.getReservation();
		if (!product.isReserved()) {
			throw new ReservationException(ErrorCode.RESERVATION_NOT_FOUND);
		}
		if (!product.isSeller(memberId) && !reservation.isBuyer(memberId)) {
			throw new ReservationException(ErrorCode.NOT_SELLER_NOR_BUYER);
		}
		return new ReservationResponse(product, reservation);
	}

	@Transactional
	public ReservationResponse update(Long productId, ReservationUpdateRequest request, Long memberId) {
		Product product = productRepository.findByIdAndDeletedFalse(productId)
			.orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));
		if (!product.isReserved()) {
			throw new ReservationException(ErrorCode.RESERVATION_NOT_FOUND);
		}
		product.verifySeller(memberId);
		Reservation reservation = product.getReservation();
		reservation.changeTime(request.getTimeToMeet());
		return new ReservationResponse(product, reservation);
	}
}
