package com.example.muse.domain.review.dto;

import com.example.muse.domain.member.Member;
import com.example.muse.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetReviewsResponseDto {

    private List<ReviewDto> reviews;
    private long page;
    private long totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
    private long totalElements;

    public static GetReviewsResponseDto from(Page<Review> reviews, Member member) {

        List<ReviewDto> reviewDtos = reviews.getContent().stream()
                .map(review -> ReviewDto.from(review, member))
                .toList();

        return GetReviewsResponseDto.builder()
                .reviews(reviewDtos)
                .page(reviews.getNumber() + 1)
                .totalPages(reviews.getTotalPages())
                .hasNext(reviews.hasNext())
                .hasPrevious(reviews.hasPrevious())
                .totalElements(reviews.getTotalElements())
                .build();
    }
}
