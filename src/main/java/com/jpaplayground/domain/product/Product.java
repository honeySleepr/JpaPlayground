package com.jpaplayground.domain.product;

import com.jpaplayground.domain.bookmark.Bookmark;
import com.jpaplayground.domain.product.exception.ProductException;
import com.jpaplayground.domain.reservation.Reservation;
import com.jpaplayground.domain.reservation.exception.ReservationException;
import com.jpaplayground.global.auditing.BaseTimeEntity;
import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.member.Member;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Product extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@NotBlank // @NotBlank만 썼을 때는 table에 not null 제약조건이 붙지않는다
	private String name;

	@NotNull
	@Positive
	private Integer price;

	@NotNull
	private Boolean deleted;

	/**
	 * `@OneToMany`(Member->Product) 단방향 관계는 DB 상에는 Many 쪽에 있는 FK를 One 쪽 객체에서 관리하는 구조가 되어버려서 추천하지 않음 그래서 영한님이 추천하신
	 * `@ManyToOne-@OneToMany` 양방향으로 변경
	 */
	@NotNull
	@CreatedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = false, name = "seller_id")
	private Member seller;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_id")
	private Reservation reservation;

	/**
	 * <h2>product 삭제 시 관련 북마크도 삭제!</h2>
	 * 1. Cascade.Type.PERSIST : soft delete 이기 때문에 REMOVE로는 안된다
	 * <br>2. bookmarks.clear() : Product의 delete 필드를 true로 바꾼다고 bookmark에 영향을 주진 못한다. 연관관계를 끊어야한다
	 * <br>3. orphanRemoval true : 이것도 붙여줘야 정상 작동한다. Cascade 옵션 없이 이것만으로도 작동할 줄 알았는데, Bookmark가 FK를 가지고 있는 Product와
	 * Member에 모두 orphan 옵션을 붙여줘도 작동하지 않았다.
	 *
	 * <br><br><h2>하지만 이 방법을 사용하면 Bookmark당 쿼리가 하나씩 나가서, 따로 delete query를 날리는 방식을 사용하는 것으로 변경하였다</h2>
	 */
	@OneToMany(mappedBy = "product")
	private final Set<Bookmark> bookmarks = new HashSet<>();

	@NotNull
	@Column(columnDefinition = "enum('SELLING','RESERVED','SOLD')")
	@Enumerated(EnumType.STRING)
	private ProductStatus status;

	private Product(String name, Integer price) {
		this.name = name;
		this.price = price;
		this.deleted = false;
		this.status = ProductStatus.SELLING;
	}

	public static Product of(String name, Integer price) {
		return new Product(name, price);
	}

	/**
	 * 이 메서드를 호출한 경우 반드시 연관된 Bookmark 들도 같이 삭제해주자
	 */
	public void delete() {
		this.deleted = true;
	}

	public void verifySeller(Long memberId) {
		if (!seller.matchesId(memberId)) {
			throw new ProductException(ErrorCode.NOT_SELLER);
		}
	}

	public void reserve(Reservation reservation) {
		this.reservation = reservation;
		this.status = ProductStatus.RESERVED;
	}

	public void update(String name, Integer price) {
		if (!name.isBlank()) {
			this.name = name;
		}
		if (price != null) {
			this.price = price;
		}
	}

	public void verifyAvailableForReservation() {
		if (status == ProductStatus.SOLD) {
			throw new ReservationException(ErrorCode.PRODUCT_SOLD);
		}
		if (reservation != null) {
			throw new ReservationException(ErrorCode.RESERVED);
		}
	}

	public void verifyAvailableForSale() {
		if (status == ProductStatus.SOLD) {
			throw new ProductException(ErrorCode.PRODUCT_SOLD);
		}
	}

	public void verifyReservationExists() {
		if (reservation == null) {
			throw new ReservationException(ErrorCode.RESERVATION_NOT_FOUND);
		}
	}

	public void verifySellerOrBuyer(Long memberId) {
		if (!seller.matchesId(memberId) && !reservation.isBuyer(memberId)) {
			throw new ReservationException(ErrorCode.NOT_SELLER_NOR_BUYER);
		}
	}

	public void deleteReservation() {
		reservation = null;
		status = ProductStatus.SELLING;
	}

	public boolean matchesId(Long id) {
		return this.id.equals(id);
	}

	public void addBookmark(Bookmark bookmark) {
		this.bookmarks.add(bookmark);
	}

	public int getBookmarkCount() {
		return this.bookmarks.size();
	}

	public void deleteBookmark(Bookmark bookmark) {
		this.bookmarks.remove(bookmark);
	}

	public void changeStatusToSold() {
		status = ProductStatus.SOLD;
	}
}
