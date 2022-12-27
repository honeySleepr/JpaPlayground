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
@Transactional(readOnly = true)
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;

	@Transactional
	public ReservationResponse save(ReservationCreateRequest request, Long productId, Long memberId) {
		Product product = productRepository.findProductById(productId)
			.orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));
		product.verifySeller(memberId);
		product.verifyAvailableForReservation();

		Member buyer = memberRepository.findById(request.getBuyerId())
			.orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
		// `getReferenceById()`를 쓸까 고민했으나, 클라이언트에서 보내주는 값이므로 검증이 필요하여 `findById()` 그대로 사용

		Reservation reservation = new Reservation(buyer, product, request.getTimeToMeet());
		reservationRepository.save(reservation);
		return new ReservationResponse(product, reservation);
	}

	@Transactional
	public ReservationResponse update(Long productId, ReservationUpdateRequest request, Long memberId) {
		Reservation reservation = reservationRepository.findByProductId(productId)
			.orElseThrow(() -> new ReservationException(ErrorCode.RESERVATION_NOT_FOUND));
		Product product = reservation.getProduct();
		product.verifySeller(memberId);

		reservation.changeTime(request.getTimeToMeet());
		return new ReservationResponse(product, reservation);
	}

	@Transactional
	public ReservationResponse delete(Long productId, Long memberId) {
		Reservation reservation = reservationRepository.findByProductId(productId)
			.orElseThrow(() -> new ReservationException(ErrorCode.RESERVATION_NOT_FOUND));
		Product product = reservation.getProduct();
		product.verifySeller(memberId);

		product.changeStatusToSelling();
		reservationRepository.delete(reservation);
		return new ReservationResponse(product, reservation);
	}

}
