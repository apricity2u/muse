package com.example.muse.domain.auth.dto;

import com.example.muse.domain.image.Image;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.member.dto.GetProfileResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String nickname;
    private String profileImageUrl;

    public static LoginResponseDto from(Member member, Image profileImage) {

        String profileImageUrl = Optional.ofNullable(profileImage)
                .map(Image::getImageUrl)
                .orElse(null);

        return LoginResponseDto.builder()
                .nickname(member.getNickname())
                .profileImageUrl(profileImageUrl)
                .build();
    }


    public static LoginResponseDto from(GetProfileResponseDto memberDto) {

        return LoginResponseDto.builder()
                .nickname(memberDto.getNickname())
                .profileImageUrl(memberDto.getProfileImageUrl())
                .build();
    }
}
