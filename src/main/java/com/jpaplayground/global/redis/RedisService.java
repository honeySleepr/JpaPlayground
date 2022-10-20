package com.jpaplayground.global.redis;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

	private final RedisTemplate<String, String> redisTemplate;

	public void saveJwtRefreshToken(Long memberId, String refreshToken) {
		redisTemplate.opsForValue().set(String.valueOf(memberId), refreshToken, Duration.ofDays(14));
	}

	public String getJwtRefreshToken(Long memberId) {
		return redisTemplate.opsForValue().get(String.valueOf(memberId));
	}
}
