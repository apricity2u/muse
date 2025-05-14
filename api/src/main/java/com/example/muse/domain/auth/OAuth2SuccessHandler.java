package com.example.muse.domain.auth;

import com.example.muse.global.security.jwt.JwtTokenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        TokenDto tokenDto = authService.loginHandler(authentication);
        Cookie cookie = new Cookie("refreshToken", tokenDto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge((int) (JwtTokenUtil.REFRESH_TOKEN_VALIDITY_MILLISECONDS / 1000));
        cookie.setPath("/api/auth/reissue");
        response.setHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addCookie(cookie);
    }
}
