package com.example.muse.domain.review.dto;

import com.example.muse.domain.like.Likes;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.member.dto.MemberProfileDto;
import com.example.muse.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewWithUserDto {
    private ReviewDto review;
    private MemberProfileDto user;

    public static ReviewWithUserDto from(Review review, Member member, String profileImageUrl) {

        Likes like = Likes.builder()
                .member(member)
                .review(review)
                .build();

        boolean isLiked = member != null && review.getLikes().contains(like);
        MemberProfileDto memberProfileDto = MemberProfileDto.from(member, profileImageUrl);

        return ReviewWithUserDto.builder()
                .review(ReviewDto.from(review, member, isLiked))
                .user(memberProfileDto)
                .build();
    }
}
