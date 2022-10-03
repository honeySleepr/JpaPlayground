package com.jpaplayground.global.oauth;

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

	/* TODO : yml 파일의 "github"가 자동으로 Enum으로 매핑된다. HOW? */
	private final Map<OAuthServer, OAuthProperties> server;

	public OAuthProperties getProperties(OAuthServer oAuthServer) {
		return server.get(oAuthServer);
	}

}
