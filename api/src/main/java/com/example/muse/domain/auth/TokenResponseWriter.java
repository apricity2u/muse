package com.example.muse.domain.auth;

import com.example.muse.global.security.jwt.JwtTokenUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class TokenResponseWriter {
    private static final String REFRESH_COOKIE_NAME = "refreshToken";
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    public void writeTokens(HttpServletResponse response,
                            String accessToken,
                            String refreshToken) {

        addAccessTokenToHeader(response, accessToken);
        addRefreshTokenToHeader(response, refreshToken, (int) (JwtTokenUtil.REFRESH_TOKEN_VALIDITY_MILLISECONDS / 1000));

    }

    private void addAccessTokenToHeader(HttpServletResponse response, String accessToken) {
        response.addHeader(AUTH_HEADER, BEARER_PREFIX + accessToken);
    }

    private void addRefreshTokenToHeader(HttpServletResponse response, String refreshToken, int refreshMaxAgeSeconds) {
        Cookie cookie = new Cookie(REFRESH_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(refreshMaxAgeSeconds);
        cookie.setPath("/api/auth/reissue");
        response.addCookie(cookie);
    }
}
