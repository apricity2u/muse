package com.example.muse.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

//    @GetMapping("/profiles/{memberId}")
//    public T updateProfile(@PathVariable Long memberId) {
//        return memberService.getProfile();
//    }
//
//    @PatchMapping("/profiles/{memberId}")
//    public T updateProfile(@PathVariable Long memberId) {
//        return memberService.updateProfile();
//    }
}
