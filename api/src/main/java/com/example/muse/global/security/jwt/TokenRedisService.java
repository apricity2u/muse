package com.example.muse.global.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String BLACK_PREFIX = "black:";
    private static final String WHITE_PREFIX = "white:";
    private final JwtTokenUtil jwtTokenUtil;

    public void addTokenToBlacklist(String jti, UUID memberId) {

        redisTemplate.opsForValue().set(
                BLACK_PREFIX + jti,
                memberId,
                JwtTokenUtil.REFRESH_TOKEN_VALIDITY_MILLISECONDS,
                TimeUnit.MILLISECONDS);
    }

    public void deleteTokenFromWhitelist(String jti) {

        redisTemplate.delete(WHITE_PREFIX + jti);
    }
    public void addTokenToWhitelist(String jti, UUID memberId) {

        redisTemplate.opsForValue().set(
                WHITE_PREFIX + jti,
                memberId,
                JwtTokenUtil.REFRESH_TOKEN_VALIDITY_MILLISECONDS,
                TimeUnit.MILLISECONDS);
    }

    private boolean isTokenBlacklisted(String jti) {

        return redisTemplate.hasKey(BLACK_PREFIX + jti);
    }

    private boolean isTokenWhitelisted(String jti) {

        return redisTemplate.hasKey(WHITE_PREFIX + jti);
    }

    public boolean validateToken(Jwt refreshToken) {

        String jti = jwtTokenUtil.getJtiFromToken(refreshToken);
        return !isTokenBlacklisted(jti) && isTokenWhitelisted(jti);
    }
}