package com.example.muse.domain.review.dto;

import com.example.muse.domain.book.dto.BookDto;
import com.example.muse.domain.member.dto.MemberProfileDto;
import com.example.muse.domain.review.ReviewCardDto;
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

    public static ReviewCardResponseDto from(ReviewCardDto dto) {

        BookDto book = BookDto.builder()
                .id(dto.getBookId())
                .imageUrl(dto.getBookImageUrl())
                .title(dto.getBookTitle())
                .author(dto.getBookAuthor())
                .publisher(dto.getBookPublisher())
                .publishedDate(dto.getBookPublishedDate())
                .isbn(dto.getBookIsbn())
                .likeCount(dto.getBookLikeCount())
                .isLike(dto.isBookLike())
                .build();

        ReviewDto review = ReviewDto.builder()
                .id(dto.getReviewId())
                .content(dto.getReviewContent())
                .imageUrl(dto.getReviewImageUrl())
                .likeCount(dto.getReviewLikeCount())
                .isLike(dto.isLike())
                .build();

        MemberProfileDto user = MemberProfileDto.builder()
                .memberId(dto.getMemberId())
                .nickname(dto.getMemberNickname())
                .profileImageUrl(dto.getMemberProfileImageUrl())
                .build();


        return ReviewCardResponseDto.builder()
                .book(book)
                .review(review)
                .user(user)
                .build();
    }
}