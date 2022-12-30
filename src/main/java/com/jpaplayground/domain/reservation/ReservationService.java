package com.jpaplayground.domain.reservation;

import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.ProductRepository;
import com.jpaplayground.domain.product.exception.ProductException;
import com.jpaplayground.domain.reservation.dto.ReservationCreateRequest;
import com.jpaplayground.domain.reservation.dto.ReservationResponse;
import com.jpaplayground.domain.reservation.dto.ReservationUpdateRequest;
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
		Product product = findProduct(productId);
		product.verifyReservationDoesNotExist();
		product.verifySeller(memberId);

		Member buyer = findMember(
			request); // 여기서는 `getReferenceById()`를 써도되지 않을까 했으나, 클라이언트에서 보내주는 값이므로 검증이 필요하여 `findById()` 그대로 사용
		Reservation reservation = new Reservation(buyer, request.getTimeToMeet());
		product.reserve(reservation);
		reservationRepository.save(reservation);
		return new ReservationResponse(product, reservation);
	}

	public ReservationResponse findByProductId(Long productId, Long memberId) {
		Product product = findProduct(productId);
		product.verifyReservationExists();
		product.verifySellerOrBuyer(memberId);

		Reservation reservation = product.getReservation();
		return new ReservationResponse(product, reservation);
	}

	@Transactional
	public ReservationResponse update(Long productId, ReservationUpdateRequest request, Long memberId) {
		Product product = findProduct(productId);
		product.verifyReservationExists();
		product.verifySeller(memberId);

		Reservation reservation = product.getReservation();
		reservation.changeTime(request.getTimeToMeet());
		return new ReservationResponse(product, reservation);
	}

	@Transactional
	public ReservationDeleteResponse delete(Long productId, Long memberId) {
		Product product = findProduct(productId);
		product.verifyReservationExists();
		product.verifySeller(memberId);

		Reservation reservation = product.getReservation();
		product.deleteReservation();
		reservationRepository.delete(reservation);
		return new ReservationDeleteResponse(product, reservation);
	}

	private Product findProduct(Long productId) {
		return productRepository.findProductById(productId)
			.orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));
	}

	private Member findMember(ReservationCreateRequest request) {
		return memberRepository.findById(request.getBuyerId())
			.orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
	}

}
