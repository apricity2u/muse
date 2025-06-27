package com.example.muse.domain.member;

import com.example.muse.domain.member.dto.GetProfileResponseDto;
import com.example.muse.domain.member.dto.MemberProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PatchMapping("/profiles/{memberId}")
    public MemberProfileDto updateProfile(@PathVariable UUID memberId,
                                          @RequestPart(value = "image", required = false) MultipartFile imageFile,
                                          @RequestPart(value = "nickname", required = false) String nickname,
                                          @AuthenticationPrincipal UUID authMemberId) {

        return memberService.updateProfile(imageFile, memberId, nickname, authMemberId);
    }
}
