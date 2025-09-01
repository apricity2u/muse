package com.example.muse.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Modifying
    @Query("DELETE FROM Likes l WHERE l.review.id = :reviewId AND l.member.id = :memberId")
    int deleteByReviewIdAndMember(@Param("reviewId") Long reviewId, @Param("memberId") UUID memberId);

    @Modifying
    @Query("DELETE FROM Likes l WHERE l.book.id = :bookId AND l.member.id = :memberId")
    int deleteByBookIdAndMember(@Param("bookId") Long bookId, @Param("memberId") UUID memberId);

    @Modifying
    @Query(value = "INSERT INTO likes (member_id, book_id) VALUES (:memberId, :bookId) ON DUPLICATE KEY UPDATE id = id", nativeQuery = true)
    void upsertBookLike(@Param("bookId") Long bookId, @Param("memberId") String memberId);

    @Modifying
    @Query(value = "INSERT INTO likes (member_id, review_id) VALUES (:memberId, :reviewId) ON DUPLICATE KEY UPDATE id = id", nativeQuery = true)
    void upsertReviewLike(@Param("reviewId") Long reviewId, @Param("memberId") String memberId);

    long countByBookId(Long bookId);

    List<Likes> findAllByBookId(Long id);

    long countByBookIdAndMemberId(Long bookId, UUID memberId);
}
