package com.example.muse.domain.auth;

import com.example.muse.global.security.jwt.JwtTokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class TokenResponseWriter {
    public static final String REFRESH_COOKIE_NAME = "refreshToken";
    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final String COOKIE_PATH = "/api/auth";

    public void writeTokens(HttpServletResponse response,
                            Jwt accessToken,
                            Jwt refreshToken) {

        response.addHeader(
                AUTH_HEADER,
                BEARER_PREFIX + accessToken.getTokenValue()
        );

        int refreshMaxAgeSeconds = (int) JwtTokenUtil.REFRESH_TOKEN_VALIDITY_MILLISECONDS / 1000;

        Cookie cookie = new Cookie(REFRESH_COOKIE_NAME, refreshToken.getTokenValue());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(refreshMaxAgeSeconds);
        cookie.setSecure(true);
        cookie.setPath(COOKIE_PATH);

        response.addCookie(cookie);
    }

    public void deleteTokens(HttpServletResponse response) {

        Cookie cookie = new Cookie(REFRESH_COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setPath(COOKIE_PATH);

        response.addCookie(cookie);
    }
}
