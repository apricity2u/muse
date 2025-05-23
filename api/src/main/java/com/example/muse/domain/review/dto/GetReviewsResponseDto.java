package com.example.muse.domain.review.dto;

import com.example.muse.domain.book.dto.BookDto;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetReviewsResponseDto {
    private List<ReviewCardResponseDto> reviews;
    private long totalPages;
    private boolean hasNext;
    private boolean hasPrevious;

    public static GetReviewsResponseDto from(Page<Review> reviews, Member member) {

        List<ReviewCardResponseDto> reviewCardResponseDtoList
                = reviews.getContent().stream()
                .map(
                        review -> ReviewCardResponseDto.from(
                                BookDto.from(review.getBook(), member),
                                ReviewDto.from(review, member)
                        )
                ).toList();


        return GetReviewsResponseDto.builder()
                .reviews(reviewCardResponseDtoList)
                .totalPages(reviews.getTotalPages())
                .hasNext(reviews.hasNext())
                .hasPrevious(reviews.hasPrevious())
                .build();
    }
}
