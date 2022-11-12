package com.jpaplayground.global.config;

import com.jpaplayground.global.login.LoginMemberArgumentResolver;
import com.jpaplayground.global.login.filter.LoginFilter;
import com.jpaplayground.global.login.interceptor.AuthenticationInterceptor;
import com.jpaplayground.global.login.jwt.AccessTokenArgumentResolver;
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

	private final AuthenticationInterceptor authenticationInterceptor;
	private final LoginFilter loginFilter;
	private final LoginMemberArgumentResolver loginMemberArgumentResolver;
	private final AccessTokenArgumentResolver accessTokenArgumentResolver;

	@Bean
	public FilterRegistrationBean<LoginFilter> setLoginFilter() {
		FilterRegistrationBean<LoginFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(loginFilter);
		filterRegistrationBean.addUrlPatterns("/login/github", "/login/naver");
		filterRegistrationBean.setOrder(0);
		return filterRegistrationBean;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authenticationInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/login/**", "/jwt/refresh", "/*.ico", "/error", "/css/**");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(loginMemberArgumentResolver);
		resolvers.add(accessTokenArgumentResolver);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
