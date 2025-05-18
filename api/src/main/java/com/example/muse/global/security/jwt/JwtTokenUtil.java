package com.example.muse.global.security.jwt;


import com.example.muse.domain.auth.TokenResponseWriter;
import com.example.muse.domain.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    public static final long REFRESH_TOKEN_VALIDITY_MILLISECONDS = 1000L * 60 * 60 * 24 * 30; // 30일
    public static final long ACCESS_TOKEN_VALIDITY_MILLISECONDS = 1000L * 60 * 60 * 24 * 7; // 개발용 7일 TODO: 30분
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public Jwt createRefreshToken(Member member) {

        return createToken(member.getId(), REFRESH_TOKEN_VALIDITY_MILLISECONDS);
    }

    public Jwt createRefreshToken(String memberId) {

        return createToken(memberId, REFRESH_TOKEN_VALIDITY_MILLISECONDS);
    }

    public Jwt createAccessToken(Member member) {

        return createToken(member.getId(), ACCESS_TOKEN_VALIDITY_MILLISECONDS);
    }

    public Jwt createAccessToken(String memberId) {

        return createToken(memberId, ACCESS_TOKEN_VALIDITY_MILLISECONDS);
    }

    private Jwt createToken(String memberId, long validityMillis) {

        try {
            UUID.fromString(memberId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid memberId format: " + memberId, e);
        }
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .issuedAt(now)
                .subject(memberId)
                .expiresAt(now.plusMillis(validityMillis))
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        );
    }

    private Jwt createToken(UUID memberId, long validityMillis) {

        return createToken(memberId.toString(), validityMillis);
    }


    public boolean validateToken(Jwt token) {

        return token != null && token.getExpiresAt().isAfter(Instant.now());
    }

    public Jwt tokenFrom(String token) {

        try {
            return jwtDecoder.decode(token);
        } catch (JwtException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String getJtiFromToken(Jwt token) {

        return token.getId();
    }

    public String getAccessTokenFromRequest(HttpServletRequest request) {

        return Optional.ofNullable(request.getHeader(TokenResponseWriter.AUTH_HEADER))
                .filter(bearerToken -> bearerToken.startsWith(TokenResponseWriter.BEARER_PREFIX))
                .map(bearerToken -> bearerToken.substring(TokenResponseWriter.BEARER_PREFIX.length()))
                .orElse(null);
    }
}