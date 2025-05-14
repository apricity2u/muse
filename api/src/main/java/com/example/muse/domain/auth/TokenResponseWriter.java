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

    /**
     * JWT 액세스 토큰과 리프레시 토큰을 HTTP 응답에 추가합니다.
     *
     * 액세스 토큰은 "Authorization" 헤더에 "Bearer " 접두사와 함께 추가되며,
     * 리프레시 토큰은 HTTP-only, Secure 쿠키로 "/api/auth/reissue" 경로에 설정됩니다.
     *
     * @param response 토큰을 추가할 HTTP 응답 객체
     * @param accessToken 클라이언트에 전달할 JWT 액세스 토큰
     * @param refreshToken 클라이언트에 전달할 JWT 리프레시 토큰
     */
    public void writeTokens(HttpServletResponse response,
                            String accessToken,
                            String refreshToken) {

        addAccessTokenToHeader(response, accessToken);
        addRefreshTokenToHeader(response, refreshToken, (int) (JwtTokenUtil.REFRESH_TOKEN_VALIDITY_MILLISECONDS / 1000));

    }

    /**
     * 액세스 토큰을 "Authorization" 헤더에 "Bearer " 접두사와 함께 추가합니다.
     *
     * @param response 토큰을 추가할 HTTP 응답 객체
     * @param accessToken 추가할 JWT 액세스 토큰
     */
    private void addAccessTokenToHeader(HttpServletResponse response, String accessToken) {
        response.addHeader(AUTH_HEADER, BEARER_PREFIX + accessToken);
    }

    /**
     * HTTP 응답에 리프레시 토큰을 HTTP-only, Secure 쿠키로 추가합니다.
     *
     * 쿠키는 "refreshToken"이라는 이름으로 설정되며, 지정된 만료 시간(초)과 "/api/auth/reissue" 경로에만 적용됩니다.
     */
    private void addRefreshTokenToHeader(HttpServletResponse response, String refreshToken, int refreshMaxAgeSeconds) {
        Cookie cookie = new Cookie(REFRESH_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(refreshMaxAgeSeconds);
        cookie.setPath("/api/auth/reissue");
        response.addCookie(cookie);
    }
}
