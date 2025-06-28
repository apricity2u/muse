package com.example.muse.domain.review.dto;

import com.example.muse.domain.book.dto.BookDto;
import com.example.muse.domain.member.dto.MemberProfileDto;
import com.example.muse.global.common.config.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCardResponseDto {
    private BookDto book;
    private ReviewDto review;
    private MemberProfileDto user;
    private static final int S3_PREFIX_LENGTH = 6;

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
                .imageUrl(processImageUrl(dto.getReviewImageUrl()))
                .likeCount(dto.getReviewLikeCount())
                .isLike(dto.isLike())
                .build();

        MemberProfileDto user = MemberProfileDto.builder()
                .memberId(dto.getMemberId())
                .nickname(dto.getMemberNickname())
                .profileImageUrl(processImageUrl(dto.getMemberProfileImageUrl()))
                .build();


        return ReviewCardResponseDto.builder()
                .book(book)
                .review(review)
                .user(user)
                .build();
    }

    private static String processImageUrl(String imageUrl) {

        return Optional.ofNullable(imageUrl)
                .map(url -> AppConstants.IMAGE_PREFIX + url.substring(S3_PREFIX_LENGTH))
                .orElse(null);
    }
}