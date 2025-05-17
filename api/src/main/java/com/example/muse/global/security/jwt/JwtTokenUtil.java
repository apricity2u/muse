package com.example.muse.global.security.jwt;


import com.example.muse.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil { //TODO: String->JWT
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public static final long REFRESH_TOKEN_VALIDITY_MILLISECONDS = 1000L * 60 * 60 * 24 * 30; // 30일
    public static final long ACCESS_TOKEN_VALIDITY_MILLISECONDS = 1000L * 60 * 60 * 24 * 7; // 개발용 7일 TODO: 30분


    public Jwt createRefreshToken(Member member) {

        return createToken(member, REFRESH_TOKEN_VALIDITY_MILLISECONDS);
    }

    public Jwt createAccessToken(Member member) {

        return createToken(member, ACCESS_TOKEN_VALIDITY_MILLISECONDS);
    }

    private Jwt createToken(Member member, long validityMillis) {

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .issuedAt(now)
                .subject(member.getId().toString())
                .expiresAt(now.plusMillis(validityMillis))
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        );
    }


    public boolean validateToken(Jwt token) {

        return token != null && token.getExpiresAt().isAfter(Instant.now());
    }

    public Jwt from(String token) {

        return jwtDecoder.decode(token);
    }

    public String getJtiFromToken(Jwt token) {

        return token.getId();
    }
}