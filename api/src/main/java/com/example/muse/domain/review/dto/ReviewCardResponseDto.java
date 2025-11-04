package com.example.muse.domain.review.dto;

import com.example.muse.domain.book.dto.BookDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCardResponseDto {
    private BookDto book;
    private ReviewDto review;

    public static ReviewCardResponseDto from(BookDto book, ReviewDto review) {

        return ReviewCardResponseDto.builder()
                .book(book)
                .review(review)
                .build();
    }
}