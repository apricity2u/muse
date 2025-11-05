package com.example.muse.global.security.handler;

import com.example.muse.global.common.dto.ApiResponse;
import com.example.muse.global.common.exception.CustomJwtException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        log.warn("Authentication exception [{} {}]: {}", request.getMethod(), request.getRequestURI(),
                authException == null ? "null" : authException.getMessage(), authException);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (authException instanceof CustomJwtException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ApiResponse<Void> errorResponse = ApiResponse.error(
                    authException.getMessage() != null && !authException.getMessage().isBlank()
                            ? authException.getMessage()
                            : "인증이 필요합니다.",
                    "UNAUTHORIZED"
            );
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }


        log.error("Unexpected authentication error", authException);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        ApiResponse<Void> errorResponse = ApiResponse.error(
                "서버 내부 처리 오류가 발생했습니다.",
                "INTERNAL_SERVER_ERROR"
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
