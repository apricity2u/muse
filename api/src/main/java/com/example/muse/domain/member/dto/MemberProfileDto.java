package com.example.muse.domain.member.dto;

import com.example.muse.domain.image.Image;
import com.example.muse.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileDto {
    private UUID memberId;
    private String nickname;
    private String profileImageUrl;

    public static MemberProfileDto from(Member member) {

        String imageUrl = member.getImages().stream()
                .max(Comparator.comparing(Image::getCreatedAt))
                .map(Image::getImageUrl)
                .orElse(null);

        return MemberProfileDto.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .profileImageUrl(imageUrl)
                .build();
    }
}
