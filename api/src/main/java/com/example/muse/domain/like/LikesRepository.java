package com.example.muse.domain.like;

import com.example.muse.domain.member.Member;
import com.example.muse.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByReviewAndMember(Review review, Member member);

    @Modifying
    @Query("DELETE FROM Likes l WHERE l.review.id = :reviewId AND l.member = :member")
    int deleteByReviewIdAndMember(@Param("reviewId") Long reviewId, @Param("member") Member member);
}
