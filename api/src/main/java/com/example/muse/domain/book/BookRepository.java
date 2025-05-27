package com.example.muse.domain.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleNormalizedContaining(String normalizedTitle);


    @Query("""
            SELECT b
            FROM Book b
            JOIN b.likes l
            WHERE l.member.id = :memberId
            GROUP BY b
            ORDER BY COUNT(l) DESC
            """)
    Page<Book> findLikedBooksOrderByLikesDesc(
            @Param("memberId") UUID memberId,
            Pageable pageable
    );

    @Query("""
            SELECT b
            FROM Book b
            JOIN b.likes l
            WHERE l.member.id = :memberId
            GROUP BY b
            ORDER BY b.publishedDate DESC
            """)
    Page<Book> findLikedBooksOrderByDateDesc(
            @Param("memberId") UUID memberId,
            Pageable pageable
    );


    Page<Book> findByReviewsMemberId(Pageable pageable, UUID memberId);
}