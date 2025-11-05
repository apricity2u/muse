package com.example.muse.domain.review.dto;

import com.example.muse.domain.book.dto.BookDto;
import com.example.muse.domain.member.dto.MemberProfileDto;
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
    private MemberProfileDto user;

    public static ReviewCardResponseDto from(BookDto book, ReviewDto review, MemberProfileDto user) {

        return ReviewCardResponseDto.builder()
                .book(book)
                .review(review)
                .user(user)
                .build();
    }
}