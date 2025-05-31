package com.example.muse.domain.auth;

import com.example.muse.domain.auth.dto.TokenDto;
import com.example.muse.domain.member.Member;
import com.example.muse.global.security.jwt.JwtTokenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private static final String REACT_SUCCESS_URL = "http://localhost:5173/login/success";
    private final AuthService authService;
    private final TokenResponseWriter tokenResponseWriter;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Member member = authService.processLogin(authentication);
        TokenDto tokenDto = authService.login(member);

        Jwt accessToken = jwtTokenUtil.tokenFrom(tokenDto.getAccessToken());
        Jwt refreshToken = jwtTokenUtil.tokenFrom(tokenDto.getRefreshToken());
        tokenResponseWriter.writeTokens(response, accessToken, refreshToken);

        response.sendRedirect(REACT_SUCCESS_URL);// TODO:????
    }
}
