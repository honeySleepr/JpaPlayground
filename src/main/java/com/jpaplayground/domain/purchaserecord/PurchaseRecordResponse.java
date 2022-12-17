package com.jpaplayground.domain.purchaserecord;

import com.jpaplayground.domain.product.Product;
import lombok.Getter;

@Getter
public class PurchaseRecordResponse {

	private final Long productId;
	private final Long sellerId;
	private final Long buyerId;
	private final Integer price;

	public PurchaseRecordResponse(PurchaseRecord purchaseRecord) {
		Product product = purchaseRecord.getProduct();
		this.productId = product.getId();
		this.sellerId = purchaseRecord.getSeller().getId();
		this.buyerId = purchaseRecord.getBuyer().getId();
		this.price = product.getPrice();
	}
}
