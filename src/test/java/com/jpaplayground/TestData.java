package com.jpaplayground;

import com.jpaplayground.product.Product;
import java.util.List;

/**
 * <a href="https://reflectoring.io/objectmother-fluent-builder/">ObjectMother Pattern</a>
 * 뭔가 이런 비슷한걸 해보고 싶었는데 이해 안돼서 일단 단순하게 간다
 */

public class TestData {

	public static List<Product> products = List.of(
		Product.of("한무무", 100_000),
		Product.of("4K모니터", 300_000),
		Product.of("노트북 파우치", 15000));

}
