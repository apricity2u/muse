package com.example.muse.domain.review.dto;

import com.example.muse.domain.book.dto.BookDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetReviewDetailResponseDto {
    private BookDto book;
    private ReviewDetailDto review;
}
