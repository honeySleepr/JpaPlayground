package com.jpaplayground.global;

import com.jpaplayground.global.login.LoginFilter;
import com.jpaplayground.global.login.LoginInterceptor;
import javax.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final LoginFilter loginFilter;
	private final LoginInterceptor loginInterceptor;

	@Bean
	public FilterRegistrationBean<Filter> setFilterRegistration() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(loginFilter);
		filterRegistrationBean.addUrlPatterns("/login/github", "/login/naver", "/login/kakao");
		filterRegistrationBean.setOrder(0);
		return filterRegistrationBean;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/login/**", "/*.ico", "/error", "/css/**");
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
