package com.example.muse.global.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static String BLACK_PREFIX = "black:";
    private static String WHITE_PREFIX = "white:";

    public void tokenSetBlackList(String jti, UUID memberId) {
        String key = BLACK_PREFIX + jti;
        redisTemplate.opsForValue().set(key, memberId, JwtTokenUtil.REFRESH_TOKEN_VALIDITY_MILLISECONDS, TimeUnit.MICROSECONDS);
    }

    public void tokenSetWhiteList(String jti, UUID memberId) {
        String key = BLACK_PREFIX + jti;
        redisTemplate.opsForValue().set(key, memberId, JwtTokenUtil.REFRESH_TOKEN_VALIDITY_MILLISECONDS, TimeUnit.MICROSECONDS);
    }
}
