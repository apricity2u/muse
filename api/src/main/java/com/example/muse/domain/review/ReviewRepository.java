package com.example.muse.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"book", "member", "image", "likes"})
    @Query("""
            SELECT r FROM Review r
            ORDER BY SIZE(r.likes) DESC
            """)
    Page<Review> findMainReviews(Pageable pageable);

    @EntityGraph(attributePaths = {"book", "member", "image", "likes"})
    @Query("""
            SELECT r
            FROM Review r
            JOIN r.book b
            WHERE r.book.id = :bookId
            ORDER BY SIZE(r.likes) DESC
            """)
    Page<Review> findByBookIdOrderByLikesDesc(Pageable pageable, @Param("bookId") Long bookId);

    @EntityGraph(attributePaths = {"book", "member", "image", "likes"})
    @Query("""
            SELECT r
            FROM Review r
            JOIN r.book b
            WHERE r.book.id = :bookId
            ORDER BY r.createdAt DESC
            """)
    Page<Review> findByBookIdOrderByDateDesc(Pageable pageable, @Param("bookId") Long bookId);

    @EntityGraph(attributePaths = {"book", "member", "image", "likes"})
    @Query("""
              SELECT r
              FROM Review r
              JOIN r.likes lm
              ON lm.member.id = :id
              LEFT JOIN r.likes la
              ORDER BY SIZE(r.likes) DESC
            """)
    Page<Review> findLikedReviewsOrderByLikesDesc(@Param("id") UUID id, Pageable pageable);

    @EntityGraph(attributePaths = {"book", "member", "image", "likes"})
    @Query("""
            SELECT r
            FROM Review r
            JOIN r.likes l
            WHERE l.member.id = :id
            ORDER BY r.createdAt DESC
            """)
    Page<Review> findLikedReviewsByMemberIdOrderByCreatedAtDesc(@Param("id") UUID id, Pageable pageable);

    @EntityGraph(attributePaths = {"book", "member", "image", "likes"})
    @Query("""
            SELECT r
            FROM Review r
            WHERE r.id = :reviewId
            """)
    Optional<Review> findReviewWithBookById(@Param("reviewId") Long reviewId);

    @EntityGraph(attributePaths = {"book", "member", "image", "likes"})
    @Query("""
            SELECT r
            FROM Review r
            LEFT JOIN r.likes l
            WHERE r.member.id = :memberId
            GROUP BY r
            ORDER BY COUNT(l) DESC
            """)
    Page<Review> findByMemberIdOrderByLikesDesc(Pageable pageable, @Param("memberId") UUID memberId);

    @EntityGraph(attributePaths = {"book", "member", "image", "likes"})
    @Query("""
            SELECT r
            FROM Review r
            WHERE r.member.id = :memberId
            ORDER BY r.createdAt DESC
            """)
    Page<Review> findByMemberIdOrderByDateDesc(Pageable pageable, @Param("memberId") UUID memberId);

    long countByMemberId(UUID memberId);
}
