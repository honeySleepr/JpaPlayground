package com.jpaplayground;

import com.jpaplayground.domain.product.Product;
import java.util.List;
import java.util.stream.Collectors;

public class TestData {

	public static final List<Product> allProducts;
	public static final List<Product> deletedProducts;
	public static final List<Product> notDeletedProducts;

	static {
		allProducts = buildAllProducts();
		deletedProducts = buildDeletedProducts();
		notDeletedProducts = buildNotDeletedProducts();
	}

	private static List<Product> buildAllProducts() {
		Product deletedProduct1 = Product.of("노트북파우치", 10000);
		Product deletedProduct2 = Product.of("와플기계", 30000);
		Product deletedProduct3 = Product.of("한무무", 100000);
		deletedProduct1.delete();
		deletedProduct2.delete();
		deletedProduct3.delete();

		return List.of(
			deletedProduct1,
			Product.of("가습기", 15000),
			Product.of("맥북에어M1", 900000),
			Product.of("버티컬마우스", 20000),
			deletedProduct2,
			Product.of("쉐이커통", 5000),
			Product.of("클라이밍초크", 10000),
			Product.of("닌텐도스위치", 250000),
			Product.of("젤다의전설", 35000),
			deletedProduct3
		);
	}

	private static List<Product> buildDeletedProducts() {
		return allProducts.stream().filter(Product::getDeleted).collect(Collectors.toList());
	}

	private static List<Product> buildNotDeletedProducts() {
		return allProducts.stream().filter(product -> !product.getDeleted()).collect(Collectors.toList());
	}
}
