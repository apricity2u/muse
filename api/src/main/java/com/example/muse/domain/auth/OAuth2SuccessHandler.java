package com.example.muse.domain.auth;

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


    /**
     * OAuth2 인증이 성공적으로 완료되었을 때 액세스 토큰과 리프레시 토큰을 생성하여 HTTP 응답에 기록합니다.
     *
     * @param request 인증 요청이 포함된 HTTP 요청
     * @param response 토큰이 기록될 HTTP 응답
     * @param authentication 인증된 사용자 정보를 포함하는 Authentication 객체
     * @throws IOException 응답 기록 중 입출력 오류가 발생한 경우
     * @throws ServletException 서블릿 처리 중 예외가 발생한 경우
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        TokenDto tokenDto = authService.processLogin(authentication);
        String refreshToken = tokenDto.getRefreshToken();
        String accessToken = tokenDto.getAccessToken();

        tokenResponseWriter.writeTokens(response, accessToken, refreshToken);
    }
}
