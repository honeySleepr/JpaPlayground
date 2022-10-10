package com.jpaplayground.global.login.oauth;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth")
public class OAuthPropertyHandler {

	private final Map<OAuthServer, OAuthProperties> server;

	public OAuthProperties getProperties(String server) {
		return this.server.get(OAuthServer.getOAuthServer(server));
	}
}
