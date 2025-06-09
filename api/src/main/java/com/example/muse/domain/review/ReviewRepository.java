package com.example.muse.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
            SELECT r FROM Review r
            LEFT JOIN r.likes l
            GROUP BY r
            ORDER BY COUNT(l) DESC
            """)
    Page<Review> findMainReviews(Pageable pageable);

    // TODO
    Page<Review> findByMemberId(Pageable pageable, UUID memberId);

    @Query(value = """
            SELECT r.*
            FROM review r
            LEFT JOIN likes l ON r.id = l.review_id
            WHERE r.member_id = :memberId
            GROUP BY r.id
            ORDER BY COUNT(l.id) DESC
            LIMIT :limit OFFSET :offset
            """,
            nativeQuery = true
    )
    List<Review> findReviewsByMemberIdOrderByLikesDesc(
            @Param("memberId") UUID memberId,
            @Param("limit") int limit,
            @Param("offset") long offset
    );

    @Query(value = """
            SELECT COUNT(DISTINCT r.id)
            FROM review r
            WHERE r.member_id = :memberId
            """,
            nativeQuery = true
    )
    long countReviewsByMemberId(@Param("memberId") UUID memberId);

    @Query("""
            SELECT r
            FROM Review r
            JOIN r.book b
            LEFT JOIN r.likes l
            WHERE r.book.id = :bookId
            GROUP BY r
            ORDER BY COUNT(l) DESC
            """)
    Page<Review> findByBookIdOrderByLikesDesc(Pageable pageable, Long bookId);

    @Query("""
            SELECT r
            FROM Review r
            JOIN r.book b
            WHERE r.book.id = :bookId
            ORDER BY r.createdAt DESC
            """)
    Page<Review> findByBookIdOrderByDateDesc(Pageable pageable, Long bookId);

    @Query("""
              SELECT r
              FROM Review r
              JOIN r.likes lm
              ON lm.member.id = :id
              LEFT JOIN r.likes la
              GROUP BY r.id
              ORDER BY COUNT(la) DESC
            """)
    Page<Review> findLikedReviewsOrderByLikesDesc(UUID id, Pageable pageable);

    @Query("""
            SELECT r
            FROM Review r
            JOIN r.likes l
            WHERE l.member.id = :id
            ORDER BY r.createdAt DESC
            """)
    Page<Review> findLikedReviewsByMemberIdOrderByCreatedAtDesc(UUID id, Pageable pageable);

    @Query("""
            SELECT r
            FROM Review r
            JOIN FETCH r.book b
            WHERE r.id = :reviewId
            """)
    Optional<Review> findReviewWithBookById(Long reviewId);

    @Query("""
            SELECT r
            FROM Review r
            LEFT JOIN r.likes l
            WHERE r.member.id = :memberId
            GROUP BY r
            ORDER BY COUNT(l) DESC
            """)
    Page<Review> findByMemberIdOrderByLikesDesc(Pageable pageable, UUID memberId);

    @Query("""
            SELECT r
            FROM Review r
            WHERE r.member.id = :memberId
            ORDER BY r.createdAt DESC
            """)
    Page<Review> findByMemberIdOrderByDateDesc(Pageable pageable, UUID memberId);
}
