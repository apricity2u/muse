package com.example.muse.domain.member.dto;

import com.example.muse.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileDto {
    private UUID memberId;
    private String nickname;
    private String profileImageUrl;

    public static MemberProfileDto from(Member member, String profileImageUrl) {

        return MemberProfileDto.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
