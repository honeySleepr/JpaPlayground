package com.jpaplayground.product;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * 커스텀 쿼리 메서드(@Query)나 영속성 컨텍스트와 관련된 테스트 등을 할때는 Repository 테스트를 작성하자<br> 그 외에는 서비스 통합테스트에서 테스트하는 게 좋을 것 같다
 */
@DataJpaTest
public class ProductRepositoryTest {

	@Autowired
	ProductRepository productRepository;
	@PersistenceContext
	EntityManager entityManager;

}
