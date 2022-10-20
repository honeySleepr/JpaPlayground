package com.jpaplayground.config;

import com.jpaplayground.global.login.LoginFilter;
import com.jpaplayground.global.login.LoginInterceptor;
import com.jpaplayground.global.login.LoginMemberArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final LoginFilter loginFilter;
	private final LoginInterceptor loginInterceptor;
	private final LoginMemberArgumentResolver loginMemberArgumentResolver;

	@Bean
	public FilterRegistrationBean<LoginFilter> setFilterRegistration() {
		FilterRegistrationBean<LoginFilter> filterRegistrationBean = new FilterRegistrationBean<>();
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

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(loginMemberArgumentResolver);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
