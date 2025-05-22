package com.example.muse.domain.auth.dto;

import com.example.muse.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String nickname;

    public static LoginResponseDto from(Member member) {

        return LoginResponseDto.builder()
                .nickname(member.getNickname())
                .build();
    }
}
