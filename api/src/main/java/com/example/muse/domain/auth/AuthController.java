package com.example.muse.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
//    private final authService authService;
//    @PostMapping("/auth/logout")
//    public T logout() {
//        return memberService.logout();
//    }
//
//    @PostMapping("/auth/reissue")
//    public T reissue() {
//        return memberService.reissue();
//    }

    @GetMapping("api/login/oauth2/code/kakao")
    public String kakaoLogin(@RequestParam(required = false) String code) {
//        return authService.kakaoLogin();
        return code;
    }
}
