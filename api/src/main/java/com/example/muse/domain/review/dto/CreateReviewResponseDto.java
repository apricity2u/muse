package com.example.muse.domain.review.dto;

import com.example.muse.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewResponseDto {

    private Long reviewId;
    private String content;
    private String imageUrl;

    public static CreateReviewResponseDto from(Review review) {

        return new CreateReviewResponseDto(
                review.getId(),
                review.getContent(),
                review.getImage() != null ? review.getImage().getImageUrl() : null
        );
    }
}
