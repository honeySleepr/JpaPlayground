package com.jpaplayground.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpaplayground.domain.product.ProductController;
import com.jpaplayground.domain.product.ProductService;
import com.jpaplayground.domain.product.dto.ProductCreateRequest;
import com.jpaplayground.global.oauth.OAuthPropertyHandler;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <a href="https://reflectoring.io/spring-boot-web-controller-test/">
 * Testing MVC Web Controllers with Spring Boot and @WebMvcTest</a>
 *
 * <br> 이 테스트를 통해 내가 얻고자 한 것은 PostMan이나 브라우저를 쓰지 않고 @RequestBody, @RequestParam
 * 등을 통한 파라미터 매핑이 잘 되는지를 확인하는 것이었다. 하지만 DTO는 자주 바뀔 수 있기 때문에 그때마다 테스트코드도 바꿔줘야한다. 이러한 유지보수 비용 때문에 Controller 테스트는 일단 중단하고,
 * 대신에 로그를 활용해봐야겠다
 * <br><a href="https://github.com/honeySleepr/JpaPlayground/pull/5#issuecomment-1250048799">관련 PR 피드백</a>
 */
@WebMvcTest(ProductController.class)
@ActiveProfiles("test")
@EnableConfigurationProperties(OAuthPropertyHandler.class)
class ProductControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	ProductService service;
	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("product 등록")
	void add() throws Exception {
		String name = "water";
		int price = 1000;
		ProductCreateRequest request = new ProductCreateRequest(name, price);

		given(service.save(any())).willReturn(request.toEntity());

		mockMvc.perform(post("/products")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name", is(name)))
			.andExpect(jsonPath("$.price", is(price)));
	}
}
