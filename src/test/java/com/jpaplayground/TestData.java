package com.jpaplayground;

import com.jpaplayground.domain.bookmark.BookmarkRepository;
import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.ProductRepository;
import com.jpaplayground.domain.purchaserecord.PurchaseRecordRepository;
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
	private final BookmarkRepository bookmarkRepository;
	private final PurchaseRecordRepository purchaseRecordRepository;
	private final HttpServletRequest httpServletRequest;
	private Member seller;
	private Member buyer;
	private Member thirdPerson;
	private Product reservedProduct;
	private Reservation reservation;
	private List<Product> allProducts; // get(0): deleted, get(1): reserved, get(2): sold

	public TestData(ProductRepository productRepository, MemberRepository memberRepository,
					ReservationRepository reservationRepository, BookmarkRepository bookmarkRepository,
					PurchaseRecordRepository purchaseRecordRepository, HttpServletRequest httpServletRequest) {
		this.productRepository = productRepository;
		this.memberRepository = memberRepository;
		this.reservationRepository = reservationRepository;
		this.bookmarkRepository = bookmarkRepository;
		this.purchaseRecordRepository = purchaseRecordRepository;
		this.httpServletRequest = httpServletRequest;
	}

	public void init() {
		clear();

		this.seller = createSellerData();
		this.buyer = createBuyerData();
		this.thirdPerson = createThirdPersonData();
		this.reservation = createReservationData();
		this.allProducts = createProductData();
		this.reservedProduct = allProducts.get(1);

		persistMemberData();
		httpServletRequest.setAttribute(LOGIN_MEMBER, seller.getId());
		persistReservationData();
		persistProductData();
	}

	private Member createSellerData() {
		return Member.builder()
			.account("testBC")
			.server(OAuthServer.NAVER)
			.build();
	}

	private Member createBuyerData() {
		return Member.builder()
			.account("account2")
			.server(OAuthServer.GITHUB)
			.build();
	}

	private Member createThirdPersonData() {
		return Member.builder()
			.account("account3")
			.server(OAuthServer.GITHUB)
			.build();
	}

	private void clear() {
		bookmarkRepository.deleteAll();
		purchaseRecordRepository.deleteAll();
		productRepository.deleteAll();
		reservationRepository.deleteAll();
		memberRepository.deleteAll();
	}

	private Reservation createReservationData() {
		return new Reservation(buyer, LocalDateTime.now());
	}

	private void persistMemberData() {
		memberRepository.save(seller);
		memberRepository.save(buyer);
		memberRepository.save(thirdPerson);
	}

	private void persistReservationData() {
		reservationRepository.save(reservation);
	}

	private List<Product> createProductData() {
		Product deleted = Product.of("노트북파우치", 10000);
		deleted.delete();
		Product reserved = Product.of("와플기계", 30000);
		reserved.reserve(reservation);
		Product sold = Product.of("가습기", 15000);
		sold.changeStatusToSold();

		return List.of(
			deleted
			, reserved
			, sold
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

	public Member getSeller() {
		return seller;
	}

	public Member getBuyer() {
		return buyer;
	}

	public Member getThirdPerson() {
		return thirdPerson;
	}

	public List<Product> getAllProducts() {
		return allProducts;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public Product getReservedProduct() {
		return reservedProduct;
	}

}
