package com.jpaplayground.global.login.oauth;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth")
public class OAuthPropertyHandler {

	private final Map<OAuthServer, OAuthProperties> server;

	public OAuthProperties getProperties(OAuthServer oAuthServer) {
		return this.server.get(oAuthServer);
	}
}
