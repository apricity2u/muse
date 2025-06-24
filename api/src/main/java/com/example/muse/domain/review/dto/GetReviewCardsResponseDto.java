package com.example.muse.domain.review.dto;

import com.example.muse.domain.book.dto.BookDto;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.member.dto.MemberProfileDto;
import com.example.muse.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetReviewCardsResponseDto {
    private List<ReviewCardResponseDto> reviews;
    private long page;
    private long totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
    private long totalElements;

    public static GetReviewCardsResponseDto from(Page<Review> reviews, Member member, String profileImageUrl) {

        List<ReviewCardResponseDto> reviewCardResponseDtoList
                = reviews.getContent().stream()
                .map(
                        review -> ReviewCardResponseDto.from(
                                BookDto.from(review.getBook(), member),
                                ReviewDto.from(review, member),
                                MemberProfileDto.from(review.getMember(), profileImageUrl)
                        )
                ).toList();


        return GetReviewCardsResponseDto.builder()
                .reviews(reviewCardResponseDtoList)
                .page(reviews.getNumber() + 1)
                .totalPages(reviews.getTotalPages())
                .hasNext(reviews.hasNext())
                .hasPrevious(reviews.hasPrevious())
                .totalElements(reviews.getTotalElements())
                .build();
    }

    public static GetReviewCardsResponseDto from(Page<Review> reviews, Member member, Map<UUID, String> profileImageMap) {

        List<ReviewCardResponseDto> reviewCardResponseDtoList = reviews.getContent().stream()
                .map(review -> {
                    String profileImageUrl = profileImageMap.get(review.getMember().getId());

                    return ReviewCardResponseDto.from(
                            BookDto.from(review.getBook(), member),
                            ReviewDto.from(review, member),
                            MemberProfileDto.from(review.getMember(), profileImageUrl)
                    );
                })
                .toList();

        return GetReviewCardsResponseDto.builder()
                .reviews(reviewCardResponseDtoList)
                .page(reviews.getNumber() + 1)
                .totalPages(reviews.getTotalPages())
                .hasNext(reviews.hasNext())
                .hasPrevious(reviews.hasPrevious())
                .totalElements(reviews.getTotalElements())
                .build();
    }
}
