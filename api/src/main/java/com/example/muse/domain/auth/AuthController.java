package com.example.muse.domain.auth;

import com.example.muse.domain.member.Member;
import com.example.muse.domain.member.MemberRepository;
import com.example.muse.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final MemberRepository memberRepository;

    @GetMapping("/login")
    public String login() {
        return "로그인 엔드포인트";
    }

    @GetMapping("/success")
    public ResponseEntity<ApiResponse<LoginResponseDto>> loginSuccess(@RequestAttribute Member member) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "로그인에 성공했습니다.", "SUCCESS", LoginResponseDto.from(member)
                )
        );
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
    @GetMapping("/test")
    public String test(@AuthenticationPrincipal Member member) {
        return member.getId()+member.getName();
    }
}
