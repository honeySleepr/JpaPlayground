package com.jpaplayground.global.login.oauth.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NaverUserInfo extends OAuthUserInfo {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final Map<String, String> userInfo;

	public NaverUserInfo(Map<String, Object> oAuthResponse) {
		super(oAuthResponse);
		Object response = oAuthResponse.get("response");
		userInfo = new HashMap<>(objectMapper.convertValue(response, new TypeReference<>() {
		}));
	}

	@Override
	public String getAccount() {
		return userInfo.get("nickname");
	}

	@Override
	public String getName() {
		return userInfo.get("name");
	}

	@Override
	public String getEmail() {
		return userInfo.get("email");
	}

	@Override
	public String getProfileImageUrl() {
		return userInfo.get("profile_image");
	}

}
