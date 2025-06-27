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
              ( select ri.imageUrl
                  from Image ri
                 where ri.review.id = r.id
                   and ri.imageType = com.example.muse.domain.image.ImageType.REVIEW
                 order by ri.createdAt desc
                 limit 1
              ),
              size(r.likes),
              case when exists(
                 select lr from Likes lr
                  where lr.review.id = r.id
                    and lr.member.id = :authMemberId
              ) then true else false end,
              b.id,
              b.imageUrl,
              b.title,
              b.author,
              b.publisher,
              b.publishedDate,
              size(b.likes),
              case when exists(
                 select lb from Likes lb
                  where lb.book.id = b.id
                    and lb.member.id = :authMemberId
              ) then true else false end,
              b.isbn,
              m.id,
              m.nickname,
              ( select pi.imageUrl
                  from Image pi
                 where pi.member.id = m.id
                   and pi.imageType = com.example.muse.domain.image.ImageType.PROFILE
                 order by pi.createdAt desc
                 limit 1
              )
            )
            from Review r
             join r.book b
             join r.member m
            order by size(r.likes) desc
            """)
    Page<ReviewCardDto> findMainReviews(
            @Param("authMemberId") UUID authMemberId,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"book", "member", "image"})
    @Query("""
            SELECT r
            FROM Review r
            JOIN r.book b
            WHERE r.book.id = :bookId
            ORDER BY SIZE(r.likes) DESC
            """)
    Page<Review> findByBookIdOrderByLikesDesc(Pageable pageable, @Param("bookId") Long bookId);

    @EntityGraph(attributePaths = {"book", "member", "image"})
    @Query("""
            SELECT r
            FROM Review r
            JOIN r.book b
            WHERE r.book.id = :bookId
            ORDER BY r.createdAt DESC
            """)
    Page<Review> findByBookIdOrderByDateDesc(Pageable pageable, @Param("bookId") Long bookId);

    @EntityGraph(attributePaths = {"book", "member", "image"})
    @Query("""
              SELECT r
              FROM Review r
              JOIN r.likes lm
              ON lm.member.id = :id
              LEFT JOIN r.likes la
              ORDER BY SIZE(r.likes) DESC
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
            LEFT JOIN r.likes l
            WHERE r.member.id = :memberId
            GROUP BY r
            ORDER BY COUNT(l) DESC
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
