package com.example.muse.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
    private final authService authService;

    @GetMapping("/login")
    public String login() {
        return "로그인";
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
