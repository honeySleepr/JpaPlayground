package com.jpaplayground.global.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE) // JSON에서 DTO로 변환하기 위해 필요하다(private일 필요는 없다)
@AllArgsConstructor // `@Query`를 이용해 DTO에 바로 매핑하기 위해 필요하다
public class JwtCredentials {

	private String encodedSecretKey;
	private String jwtRefreshToken;
}
