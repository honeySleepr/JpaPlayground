package com.jpaplayground;

import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.ProductRepository;
import com.jpaplayground.global.login.oauth.OAuthServer;
import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TestData {

	private final ProductRepository productRepository;
	private final MemberRepository memberRepository;
	private final List<Product> allProducts;
	private final Member member1;

	public TestData(ProductRepository productRepository, MemberRepository memberRepository) {
		this.productRepository = productRepository;
		this.memberRepository = memberRepository;
		this.allProducts = createProductData();
		this.member1 = createMember1();
	}

	public void init() {
		clear();
		persistMemberData();
		persistProductData();
	}

	private void clear() {
		productRepository.deleteAll();
	}

	private Member createMember1() {
		return Member.builder()
			.account("testBC")
			.name("캉캉")
			.email("spam@naver.com")
			.profileImageUrl("https://phinf.pstatic.net/contact/20220721_150/1658381007616hcqNL_JPEG/image.jpg")
			.server(OAuthServer.NAVER)
			.build();
	}

	private void persistMemberData() {
		memberRepository.save(member1);
	}

	private List<Product> createProductData() {
		Product deleted1 = Product.of("노트북파우치", 10000);
		Product deleted2 = Product.of("와플기계", 30000);
		Product deleted3 = Product.of("한무무", 100000);
		deleted1.changeDeletedState(true);
		deleted2.changeDeletedState(true);
		deleted3.changeDeletedState(true);

		return List.of(
			deleted1
			, Product.of("가습기", 15000)
			, Product.of("맥북에어M1", 900000)
			, Product.of("버티컬마우스", 20000)
			, deleted2
			, Product.of("쉐이커통", 5000)
			, Product.of("클라이밍초크", 10000)
			, Product.of("닌텐도스위치", 250000)
			, Product.of("젤다의전설", 35000)
			, deleted3);
	}

	private void persistProductData() {
		productRepository.saveAll(allProducts);
	}

	public List<Product> getAllProducts() {
		return allProducts;
	}

	public Member getMember1() {
		return member1;
	}
}
