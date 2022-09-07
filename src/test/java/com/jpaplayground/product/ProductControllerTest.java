package com.jpaplayground.product;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpaplayground.product.dto.ProductCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * <a href="https://reflectoring.io/spring-boot-web-controller-test/">
 * Testing MVC Web Controllers with Spring Boot and @WebMvcTest</a>
 *
 * <br> 이 테스트를 통해 내가 얻고자 하는건, PostMan이나 브라우저를 쓰지 않고 http 입력을 주어서 @RequestBody, @RequestParam 등을 통한
 * 매핑이 잘 되는지를 확인하고 싶은 것이기 때문에 Web계층을 테스트하는데 필요한 <br>Bean들만 가져오는 @WebMvcTest를 사용하기로 하였다.
 * <br>즉, @WebMvcTest는 단위 테스트가 아니라 컨트롤러와 웹 계층에 한정된 통합테스트라고 보면 될 것 같다
 */
@WebMvcTest(ProductController.class)
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

		when(service.save(any())).thenReturn(request.toEntity());

		mockMvc.perform(post("/products")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name", is(name)))
			.andExpect(jsonPath("$.price", is(price)));
	}

}
