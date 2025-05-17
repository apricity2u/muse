package com.example.muse.global.security.jwt;


import com.example.muse.domain.member.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil { //TODO: String->JWT

    public static final long REFRESH_TOKEN_VALIDITY_MILLISECONDS = 1000L * 60 * 60 * 24 * 30; // 30일
    public static final long ACCESS_TOKEN_VALIDITY_MILLISECONDS = 1000L * 60 * 60 * 24 * 7; // 개발용 7일 TODO: 30분

    @Value("${JWT_SECRET}")
    private String secretKeyBase64;
    private SecretKey secretKey;

    @PostConstruct
    protected void init() {

        secretKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(secretKeyBase64)
        );
    }


    private String createToken(String subject, long validityMilliSeconds) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityMilliSeconds);
        String jti = UUID.randomUUID().toString();

        return Jwts.builder()
                .setId(jti)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Member member) {

        String refreshToken = createToken(String.valueOf(member.getId()), REFRESH_TOKEN_VALIDITY_MILLISECONDS);
        String jti = getClaims(refreshToken).getId();

        return refreshToken;
    }

    public String createAccessToken(Member member) {

        return createToken(String.valueOf(member.getId()), ACCESS_TOKEN_VALIDITY_MILLISECONDS);
    }

    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims getClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public String getJtiFromToken(String refreshToken) {

        return getClaims(refreshToken).getId();
    }
}