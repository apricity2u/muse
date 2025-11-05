package com.example.muse.global.security.jwt;


import com.example.muse.global.common.exception.CustomJwtException;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Configuration
public class JwtConfig {
    @Value("${JWT_SECRET}")
    private String secretKeyBase64;

    @Bean
    public SecretKey jwtSecretKey() {
        if (secretKeyBase64 == null || secretKeyBase64.trim().isEmpty()) {

            log.error("JWT secret key is not set");
            throw new CustomJwtException();
        }

        try {
            byte[] keyBytes = Base64.getDecoder().decode(secretKeyBase64);

            if (keyBytes.length < 32) {

                log.error("JWT secret key is too short (min 32 bytes required)");
                throw new CustomJwtException();
            }

            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {

            byte[] keyBytes = secretKeyBase64.getBytes(StandardCharsets.UTF_8);


            if (keyBytes.length < 32) {

                log.error("JWT secret key is too short (min 32 bytes required)");
                throw new CustomJwtException();
            }

            return Keys.hmacShaKeyFor(keyBytes);
        }
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {

        JWKSource<SecurityContext> jwkSource = new ImmutableSecret<>(jwtSecretKey);
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {

        return NimbusJwtDecoder.withSecretKey(jwtSecretKey).build();
    }
}
