package com.example.muse.global.security.config;

public class SecurityPathConfig {

    public static final String[] PUBLIC_POST_URLS = {
            "/api/auth/*",
    };

    public static final String[] PUBLIC_GET_URLS = {
            "/api/profiles/**",
            "/api/auth/*",
    };
}
