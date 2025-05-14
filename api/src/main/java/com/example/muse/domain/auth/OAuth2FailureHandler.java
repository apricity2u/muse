package com.example.muse.domain.auth;

import com.example.muse.global.common.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * OAuth2 인증 실패 시 JSON 형식의 401 Unauthorized 응답을 반환합니다.
     *
     * 인증 실패가 발생하면 고정된 에러 메시지와 에러 코드를 포함한 JSON 응답을 전송합니다.
     *
     * @param request 인증 요청 정보가 담긴 HttpServletRequest
     * @param response 인증 실패 응답을 작성할 HttpServletResponse
     * @param exception 인증 실패 원인을 담고 있는 AuthenticationException
     * @throws IOException 응답 작성 중 입출력 오류가 발생한 경우
     * @throws ServletException 서블릿 처리 중 예외가 발생한 경우
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiResponse<Void> ErrorResponse = ApiResponse.error("로그인에 실패했습니다.", "UNAUTHORIZED");
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse));
    }
}
