package com.example.muse.domain.auth;

import com.example.muse.domain.auth.dto.LoginResponseDto;
import com.example.muse.domain.member.Member;
import com.example.muse.global.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public String login() {
        return "로그인 엔드포인트";
    }

    @GetMapping("/success")
    public ResponseEntity<ApiResponse<LoginResponseDto>> loginSuccess(@RequestAttribute Member member) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "로그인에 성공했습니다.", "SUCCESS",
                        authService.getLoginWithProfile(member)
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@CookieValue(name = TokenResponseWriter.REFRESH_COOKIE_NAME) String refreshToken, HttpServletResponse response) {

        authService.logout(refreshToken, response);
        return ResponseEntity.ok(
                ApiResponse.ok(
                        "로그아웃에 성공했습니다.", "SUCCESS", null
                )
        );
    }


    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<LoginResponseDto>> reissue(
            @CookieValue(name = TokenResponseWriter.REFRESH_COOKIE_NAME) String refreshToken,
            HttpServletResponse response) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "토큰을 재발급했습니다.", "SUCCESS", authService.reissue(refreshToken, response)
                )
        );
    }

}
