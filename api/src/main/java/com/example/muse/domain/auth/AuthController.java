package com.example.muse.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    /**
     * 로그인 엔드포인트에 대한 간단한 안내 메시지를 반환합니다.
     *
     * @return "로그인 엔드포인트"라는 문자열
     */
    @GetMapping("/login")
    public String login() {
        return "로그인 엔드포인트";
    }
//    @PostMapping("/logout")
//    public T logout() {
//        return memberService.logout();
//    }
//
//    @PostMapping("/reissue")
//    public T reissue() {
//        return memberService.reissue();
//    }
}
