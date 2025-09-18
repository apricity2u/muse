package com.example.muse.global.security.config;

public class SecurityPathConfig {

    public static final String[] PUBLIC_POST_URLS = {
            "/api/auth/*",
    };

    public static final String[] PUBLIC_GET_URLS = {
            "/api/auth/*",
            "/api/oauth2/**",
            "/api/books",
            "/api/books/*",
            "/api/books/*/reviews",
            "/api/books/*/reviews/*",
            "/api/reviews",
            "/api/users/*/reviews",
            "/api/users/*/books",
            "/api/profiles/*",
            "/api/swagger/**",
            "/api/swagger-ui/**",
            "/api-docs/**",
            "/actuator/**"
    };

}
