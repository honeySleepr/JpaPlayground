package com.jpaplayground.domain.purchaserecord;

import com.jpaplayground.domain.product.Product;
import com.jpaplayground.domain.product.ProductRepository;
import com.jpaplayground.domain.product.exception.ProductException;
import com.jpaplayground.global.exception.ErrorCode;
import com.jpaplayground.global.member.Member;
import com.jpaplayground.global.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseRecordService {

	private final ProductRepository productRepository;
	private final PurchaseRecordRepository purchaseRecordRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public PurchaseRecordResponse sellProduct(Long memberId, Long productId, Long buyerId) {
		Product product = productRepository.findProductById(productId)
			.orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

		product.verifySeller(memberId);
		product.changeStatusToSold();

		Member buyer = memberRepository.getReferenceById(buyerId);
		PurchaseRecord purchaseRecord = new PurchaseRecord(buyer, product);
		PurchaseRecord save = purchaseRecordRepository.save(purchaseRecord);

		return new PurchaseRecordResponse(save);
	}
}
