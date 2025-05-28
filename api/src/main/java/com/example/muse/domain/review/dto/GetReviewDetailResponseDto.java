package com.example.muse.domain.review.dto;

import com.example.muse.domain.book.dto.BookDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetReviewDetailResponseDto {
    private BookDto book;
    private ReviewDetailDto review;

    public static GetReviewDetailResponseDto from(BookDto bookDto, ReviewDetailDto reviewDto) {

        return new GetReviewDetailResponseDto(bookDto, reviewDto);
    }
}
