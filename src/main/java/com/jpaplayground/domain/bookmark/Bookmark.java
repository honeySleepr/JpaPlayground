package com.jpaplayground.domain.bookmark;

import com.jpaplayground.domain.product.Product;
import com.jpaplayground.global.auditing.BaseTimeEntity;
import com.jpaplayground.global.member.Member;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Bookmark extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// @CreatedBy가 붙은채로는 한 Product에 여러 Bookmark를 등록하는 테스트를 하기 어려워서 제거하였다.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY) // Product에서는 bookmark를 참조할 필요가 없으므로 ManyToOne 단방향 매핑이면 될 것 같다.
	@JoinColumn(name = "product_id")
	private Product product;

	public Bookmark(Product product, Member member) { // 연관관계 편의메서드도 같이
		product.addBookmark(this);
		member.addBookmark(this);
		this.product = product;
		this.member = member;
	}

	public boolean matchesProduct(Product product) {
		return this.product.matchesId(product.getId());
	}

	public void delete() {
		product.deleteBookmark(this);
		member.deleteBookmark(this);
		product = null;
		member = null;
	}
}
