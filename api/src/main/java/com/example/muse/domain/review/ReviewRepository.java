package com.example.muse.domain.review;

import com.example.muse.domain.review.dto.ReviewCardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, Long> {


    @Query("""
            select new com.example.muse.domain.review.dto.ReviewCardDto(
              r.id,
              r.content,
              ri.imageUrl,
              SIZE(r.likes),
              CASE WHEN EXISTS(
                 SELECT lr FROM Likes lr
                  WHERE lr.review.id = r.id
                    AND lr.member.id = :authMemberId
              ) THEN true ELSE false END,
              b.id,
              b.imageUrl,
              b.title,
              b.author,
              b.publisher,
              b.publishedDate,
              size(b.likes),
              CASE WHEN EXISTS(
                 SELECT lb FROM Likes lb
                  WHERE lb.book.id = b.id
                    AND lb.member.id = :authMemberId
              ) THEN true ELSE false END,
              b.isbn,
              m.id,
              m.nickname,
              ( SELECT pi.imageUrl
                  FROM Image pi
                 WHERE pi.member.id = m.id
                   AND pi.imageType = com.example.muse.domain.image.ImageType.PROFILE
                 ORDER BY pi.createdAt DESC
                 LIMIT 1
              )
            )
            FROM Review r
             LEFT JOIN r.image ri
             JOIN r.book b
             JOIN r.member m
            ORDER BY SIZE(r.likes) DESC, r.id
            """)
    Page<ReviewCardDto> findMainReviews(@Param("authMemberId") UUID authMemberId, Pageable pageable);

    @EntityGraph(attributePaths = {"book", "member", "image"})
    @Query("""
            SELECT r
            FROM Review r
            JOIN r.book b
            WHERE b.id = :bookId
            ORDER BY SIZE(r.likes) DESC, r.id
            """)
    Page<Review> findByBookIdOrderByLikesDesc(Pageable pageable, @Param("bookId") Long bookId);

    @EntityGraph(attributePaths = {"book", "member", "image"})
    @Query("""
            SELECT r
            FROM Review r
            JOIN r.book b
            WHERE b.id = :bookId
            ORDER BY r.createdAt DESC
            """)
    Page<Review> findByBookIdOrderByDateDesc(Pageable pageable, @Param("bookId") Long bookId);

    @EntityGraph(attributePaths = {"book", "member", "image"})
    @Query("""
              SELECT r
              FROM Review r
              JOIN r.likes l WITH l.member.id = :id
              ORDER BY SIZE(r.likes) DESC, r.id
            """)
    Page<Review> findLikedReviewsOrderByLikesDesc(@Param("id") UUID id, Pageable pageable);

    @EntityGraph(attributePaths = {"book", "member", "image"})
    @Query("""
            SELECT r
            FROM Review r
            JOIN r.likes l
            WHERE l.member.id = :id
            ORDER BY r.createdAt DESC
            """)
    Page<Review> findLikedReviewsByMemberIdOrderByCreatedAtDesc(@Param("id") UUID id, Pageable pageable);

    @EntityGraph(attributePaths = {"book", "member", "image"})
    @Query("""
            SELECT r
            FROM Review r
            WHERE r.id = :reviewId
            """)
    Optional<Review> findReviewWithBookById(@Param("reviewId") Long reviewId);

    @EntityGraph(attributePaths = {"book", "member", "image"})
    @Query("""
            SELECT r
            FROM Review r
            WHERE r.member.id = :memberId
            ORDER BY SIZE(r.likes) DESC, r.id
            """)
    Page<Review> findByMemberIdOrderByLikesDesc(Pageable pageable, @Param("memberId") UUID memberId);

    @EntityGraph(attributePaths = {"book", "member", "image"})
    @Query("""
            SELECT r
            FROM Review r
            WHERE r.member.id = :memberId
            ORDER BY r.createdAt DESC
            """)
    Page<Review> findByMemberIdOrderByDateDesc(Pageable pageable, @Param("memberId") UUID memberId);

    long countByMemberId(UUID memberId);
}
