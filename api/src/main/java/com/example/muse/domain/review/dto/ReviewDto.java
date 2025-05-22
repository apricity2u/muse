package com.example.muse.domain.review.dto;

import com.example.muse.domain.review.Review;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private String content;
    private String imageUrl;
    private long likeCount;
    private boolean isLiked;

    public static ReviewDto from(Review review, @Nullable UUID memberId) {
        boolean isLiked = memberId != null &&
                review.getLikes().stream().anyMatch(like -> like.getMember().getId().equals(memberId));

        return ReviewDto.builder()
                .id(review.getId())
                .content(review.getContent())
                .imageUrl(review.getImage().getImageUrl())
                .likeCount(review.getLikes().size())
                .isLiked(isLiked)
                .build();
    }
}
