package com.example.muse.domain.review.dto;

import com.example.muse.domain.like.Likes;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private String content;
    private String imageUrl;
    private long likeCount;
    private boolean isLike;

    public static ReviewDto from(Review review, Member member) {

        Likes likes = Likes.builder()
                .member(member)
                .review(review)
                .build();

        boolean isLiked = member != null &&
                review.getLikes().contains(likes);

        return ReviewDto.builder()
                .id(review.getId())
                .content(review.getContent())
                .imageUrl(review.getImage() != null ? review.getImage().getImageUrl() : null)
                .likeCount(review.getLikes().size())
                .isLike(isLiked)
                .build();
    }

    public static ReviewDto from(Review review, Member member, boolean isLiked) {


        return ReviewDto.builder()
                .id(review.getId())
                .content(review.getContent())
                .imageUrl(review.getImage() != null ? review.getImage().getImageUrl() : null)
                .likeCount(review.getLikes().size())
                .isLike(isLiked)
                .build();
    }
}
