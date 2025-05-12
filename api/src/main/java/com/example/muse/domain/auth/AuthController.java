package com.example.muse.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final authService authService;
//    @PostMapping("/auth/logout")
//    public T logout() {
//        return memberService.logout();
//    }
//
//    @PostMapping("/auth/reissue")
//    public T reissue() {
//        return memberService.reissue();
//    }
}
