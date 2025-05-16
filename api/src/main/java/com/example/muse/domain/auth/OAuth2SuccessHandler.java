package com.example.muse.domain.auth;

import com.example.muse.domain.member.Member;
import com.example.muse.global.security.jwt.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
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
    private final TokenResponseWriter tokenResponseWriter;
    private final JwtTokenUtil jwtTokenUtil;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        Member member = authService.processLogin(authentication);
        String refreshToken = jwtTokenUtil.createRefreshToken(member);
        String accessToken = jwtTokenUtil.createAccessToken(member);

        tokenResponseWriter.writeTokens(response, accessToken, refreshToken);
        LoginResponseDto loginResponseDto = LoginResponseDto.from(member);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(loginResponseDto));
    }
}
