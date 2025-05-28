package com.example.muse.domain.review.dto;

import com.example.muse.domain.image.dto.ImageDto;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDetailDto {
    private Long reviewId;
    private UUID memberId;
    private String content;
    private String createdAt;
    private String updatedAt;
    private ImageDto image;
    private boolean isLike;

    public static ReviewDetailDto from(Review review, Member member) {

        boolean isLike = member != null &&
                review.getLikes().stream().anyMatch(like -> like.getMember().getId().equals(member.getId()));

        return new ReviewDetailDto(
                review.getId(),
                review.getMember().getId(),
                review.getContent(),
                review.getCreatedAt().toString(),
                review.getUpdatedAt().toString(),
                ImageDto.from(review.getImage()),
                isLike
        );
    }
}
