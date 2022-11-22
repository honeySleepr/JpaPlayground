package com.jpaplayground.domain.bookmark;

import com.jpaplayground.domain.product.Product;
import com.jpaplayground.global.member.Member;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Bookmark {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(updatable = false)
	@CreatedDate
	private LocalDateTime createdAt;

	// @CreatedBy는 여기서는 득보다 실이 많은 것 같아서 포기(테스트에 httpServletRequest 사용해야함, 연관관계 편의메서드 하나로 product와 member를 같이 관리해주기 애매해짐)
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
