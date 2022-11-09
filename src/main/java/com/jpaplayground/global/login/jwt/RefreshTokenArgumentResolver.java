package com.jpaplayground.global.login.jwt;

import com.jpaplayground.global.login.LoginUtils;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
public class RefreshTokenArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RefreshToken.class);
	}

	@Override
	public String resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
								  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.debug("@RefreshToken");
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

		return request.getHeader(LoginUtils.HEADER_REFRESH_TOKEN);
	}
}
