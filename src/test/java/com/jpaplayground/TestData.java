package com.jpaplayground;

import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.ProductRepository;
import com.jpaplayground.domain.reservation.Reservation;
import com.jpaplayground.domain.reservation.ReservationRepository;
import static com.jpaplayground.global.login.LoginUtils.LOGIN_MEMBER;
import com.jpaplayground.global.login.oauth.OAuthServer;
import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class TestData {

	private final ProductRepository productRepository;
	private final MemberRepository memberRepository;
	private final ReservationRepository reservationRepository;
	private final HttpServletRequest httpServletRequest;
	private List<Member> allMembers;
	private List<Reservation> allReservations;
	private List<Product> allProducts;

	public TestData(ProductRepository productRepository, MemberRepository memberRepository,
		ReservationRepository reservationRepository, HttpServletRequest httpServletRequest) {
		this.productRepository = productRepository;
		this.memberRepository = memberRepository;
		this.reservationRepository = reservationRepository;
		this.httpServletRequest = httpServletRequest;
	}

	public void init() {
		clear();

		this.allMembers = createMemberData();
		this.allReservations = createReservationData();
		this.allProducts = createProductData();
		persistMemberData();
		Long id = allMembers.get(0).getId();
		httpServletRequest.setAttribute(LOGIN_MEMBER, id); /* 현재 로그인 된 멤버를 지정하기 위함 */
		persistReservationData();
		persistProductData();
	}

	private void clear() {
		productRepository.deleteAll();
	}

	private List<Reservation> createReservationData() {
		Member member2 = allMembers.get(1);
		return List.of(
			new Reservation(member2, LocalDateTime.now())
		);
	}

	private List<Member> createMemberData() {
		return List.of(
			Member.builder()
				.account("testBC")
				.name("캉캉")
				.email("spam@naver.com")
				.profileImageUrl("image1.jpg")
				.server(OAuthServer.NAVER)
				.build(),
			Member.builder()
				.account("account2")
				.name("BC2")
				.email("spam2@naver.com")
				.profileImageUrl("image2.jpg")
				.server(OAuthServer.GITHUB)
				.build());
	}

	private void persistMemberData() {
		memberRepository.saveAll(allMembers);
	}

	private void persistReservationData() {
		reservationRepository.saveAll(allReservations);
	}

	private List<Product> createProductData() {
		Reservation reservation = allReservations.get(0);

		Product deleted = Product.of("노트북파우치", 10000);
		deleted.changeDeletedState(true);
		Product reserved = Product.of("와플기계", 30000);
		reserved.reserve(reservation);

		return List.of(
			deleted
			, reserved
			, Product.of("가습기", 15000)
			, Product.of("맥북에어M1", 900000)
			, Product.of("버티컬마우스", 20000)
			, Product.of("쉐이커통", 5000)
			, Product.of("클라이밍초크", 10000)
			, Product.of("닌텐도스위치", 250000)
			, Product.of("젤다의전설", 35000)
			, Product.of("한무무", 100000));
	}

	private void persistProductData() {
		productRepository.saveAll(allProducts);
	}

	public List<Product> getAllProducts() {
		return allProducts;
	}

	public List<Member> getAllMembers() {
		return allMembers;
	}
}
