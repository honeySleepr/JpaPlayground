package com.jpaplayground.global.member;

import com.jpaplayground.domain.bookmark.Bookmark;
import com.jpaplayground.domain.product.Product;
import com.jpaplayground.global.auditing.BaseTimeEntity;
import com.jpaplayground.global.login.oauth.OAuthServer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(updatable = false)
	private String account;

	@Embedded
	private OptionalInfo optionalInfo;

	@NotNull
	@Column(updatable = false)
	@Enumerated(EnumType.STRING)
	private OAuthServer server;

	@NotNull
	private Boolean loggedIn;

	@OneToMany(mappedBy = "seller")
	private final List<Product> products = new ArrayList<>();

	@OneToMany(mappedBy = "member")
	private final Set<Bookmark> bookmarks = new HashSet<>();

	/**
	 * `@Builder`를 클래스에 붙이면 모든 필드에 대한 빌더메서드가 만들어지지만, 메서드나 생성자에 붙이면 인자들에 대해서만 빌더 메서드가 만들어진다.
	 */
	@Builder
	private Member(String account, String name, String email, String profileImageUrl, OAuthServer server) {
		this.account = account;
		this.optionalInfo = new OptionalInfo(name, email, profileImageUrl);
		this.server = server;
		this.loggedIn = true;
	}

	/**
	 * 일반 정적 팩토리 메서드에서는 여기에서 new를 이용해 생성자를 호출하여 반환한다 (ex: `new Product(name, quantity, price)`) 하지만 이때 생성자에서 같은 타입인 인자의
	 * 순서가 바뀐다면(ex: price<->quantity `Product(String name, Integer price, Integer quantity)`) 입력된 quantity가 price가 되고,
	 * price가 quantity가 되지만 컴파일 에러로 잡아내지 못한다. 그래서 builder를 사용한다. 생성자 시그니처에서 price, quantity 위치가 바뀌어도 price는 price()에,
	 * quantity는 quantity()에 들어간다
	 */
	public static Member of(String account, String name, String email, String profileImageUrl) {
		return Member.builder()
			.account(account)
			.name(name)
			.email(email)
			.profileImageUrl(profileImageUrl)
			.build();
	}

	public void setServer(OAuthServer server) {
		this.server = server;
	}

	public Member logInAndUpdateInfo(Member member) {
		this.optionalInfo = new OptionalInfo(member);
		this.loggedIn = true;
		return this;
	}

	public void logOut() {
		this.loggedIn = false;
	}

	public boolean matchesId(Long id) {
		return this.id.equals(id);
	}

	public void addBookmark(Bookmark bookmark) {
		this.bookmarks.add(bookmark);
	}

	public void deleteBookmark(Bookmark bookmark) {
		this.bookmarks.remove(bookmark);
	}
}
