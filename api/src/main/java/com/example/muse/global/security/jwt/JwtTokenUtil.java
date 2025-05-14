package com.example.muse.global.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class JwtTokenUtil {
    public static final long REFRESH_TOKEN_VALIDITY_MILLISECONDS = 1000L * 60 * 60 * 24 * 30;
    public static final long ACCESS_TOKEN_VALIDITY_MILLISECONDS = 1000L * 60 * 60 * 24 * 7; // 개발용 7일

    @Value("${JWT_SECRET}")
    private String secretKeyBase64;
    private SecretKey secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(secretKeyBase64)
        );
    }


    private String createToken(Authentication authentication, long validityMilliSeconds) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityMilliSeconds);
        String jti = UUID.randomUUID().toString();

        return Jwts.builder()
                .setId(jti)
                .setSubject(authentication.getName())
                .claim("roles", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {

        return createToken(authentication, REFRESH_TOKEN_VALIDITY_MILLISECONDS);
    }

    public String createAccessToken(Authentication authentication) {

        return createToken(authentication, ACCESS_TOKEN_VALIDITY_MILLISECONDS);
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

    public Authentication getAuthentication(String accessToken) {

        Claims claims = getClaims(accessToken);

        List<GrantedAuthority> authorities = claims.get("roles", List.class).stream()
                .map(role -> new SimpleGrantedAuthority((String) role))
                .toList();

        return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
    }
}