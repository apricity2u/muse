package com.example.muse.domain.auth;

import com.example.muse.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private String nickname;

    public static LoginResponseDto from(Member member) {

        return LoginResponseDto.builder()
                .nickname(member.getNickname())
                .build();
    }
}
