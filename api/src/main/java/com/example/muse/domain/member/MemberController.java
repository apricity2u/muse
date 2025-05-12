package com.example.muse.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/auth/logout")
    public T logout() {
        return memberService.logout();
    }

    @PostMapping("/auth/reissue")
    public T reissue() {
        return memberService.reissue();
    }

    @GetMapping("/profiles/{memberId}")
    public T updateProfile(@PathVariable Long memberId) {
        return memberService.getProfile();
    }

    @PatchMapping("/profiles/{memberId}")
    public T updateProfile(@PathVariable Long memberId) {
        return memberService.updateProfile();
    }
}
