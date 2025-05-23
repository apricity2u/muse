package com.example.muse.domain.member.dto;

import com.example.muse.domain.image.Image;
import com.example.muse.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProfileResponseDto {
    private UUID memberId;
    private String nickname;
    private String profileImageUrl;
    private long reviewCount;

    public static GetProfileResponseDto from(Member member, Image profileImage) {

        String profileImageUrl = Optional.ofNullable(profileImage)
                .map(Image::getImageUrl)
                .orElse(null);

        long reviewCount = member.getReviews().size();

        return GetProfileResponseDto.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .profileImageUrl(profileImageUrl)
                .reviewCount(reviewCount)
                .build();
    }
}
