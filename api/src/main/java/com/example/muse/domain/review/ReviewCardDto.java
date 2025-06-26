package com.example.muse.domain.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCardDto {
    private Long reviewId;
    private String reviewContent;
    private String reviewImageUrl;
    private long reviewLikeCount;
    private boolean isLike;
    private Long bookId;
    private String bookImageUrl;
    private String bookTitle;
    private String bookAuthor;
    private String bookPublisher;
    private LocalDate bookPublishedDate;
    private long bookLikeCount;
    private boolean isBookLike;
    private String bookIsbn;
    private UUID memberId;
    private String memberNickname;
    private String memberProfileImageUrl;
}
