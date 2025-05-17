package com.example.muse.domain.auth;

import com.example.muse.domain.member.Member;
import com.example.muse.global.security.jwt.JwtTokenUtil;
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
    private static final String FORWARD_URL = "/api/auth/success";


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Member member = authService.processLogin(authentication);
        String refreshToken = jwtTokenUtil.createRefreshToken(member);
        String accessToken = jwtTokenUtil.createAccessToken(member);
        tokenResponseWriter.writeTokens(response, accessToken, refreshToken);

        request.setAttribute("member", member);
        request.getRequestDispatcher(FORWARD_URL)
                .forward(request, response);
    }
}
