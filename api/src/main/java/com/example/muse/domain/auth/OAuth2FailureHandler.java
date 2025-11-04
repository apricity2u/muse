package com.example.muse.domain.auth;

import com.example.muse.global.common.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        log.error("OAuth2 로그인 실패: 예외 종류 = {}, 메시지 = {}",
                exception.getClass().getSimpleName(),
                exception.getMessage());

        ApiResponse<Void> ErrorResponse = ApiResponse.error("로그인에 실패했습니다.", "UNAUTHORIZED");
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse));
    }
}
