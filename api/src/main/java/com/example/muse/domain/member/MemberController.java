package com.example.muse.domain.member;

import com.example.muse.domain.member.dto.GetProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/profiles/{memberId}")
    public GetProfileResponseDto getProfile(@PathVariable UUID memberId) {

        return memberService.getProfile(memberId);
    }
//
//    @PatchMapping("/profiles/{memberId}")
//    public T updateProfile(@PathVariable Long memberId) {
//        return memberService.updateProfile();
//    }
}
