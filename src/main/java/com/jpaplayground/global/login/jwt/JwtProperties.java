package com.jpaplayground.global.login.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

	private final String encodedSecretKey;
	private final SecretKey secretKey;

	public JwtProperties(String encodedSecretKey) {
		this.encodedSecretKey = encodedSecretKey;
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodedSecretKey));
	}
}
