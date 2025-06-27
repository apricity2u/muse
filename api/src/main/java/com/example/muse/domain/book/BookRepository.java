package com.example.muse.domain.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(value = """
            SELECT *
            FROM (
                SELECT *,
                    CASE
                        WHEN title_normalized LIKE CONCAT(:query, '%') THEN 0
                        ELSE 1
                    END AS prefix_order,
                    MATCH(title_normalized) AGAINST(:query IN NATURAL LANGUAGE MODE) AS relevance
                FROM book
                WHERE MATCH(title_normalized) AGAINST(:query IN NATURAL LANGUAGE MODE)
            ) AS result
            ORDER BY prefix_order ASC, relevance DESC
            LIMIT 10
            """, nativeQuery = true)
    List<Book> findByTitleContaining(@Param("query") String query);


    @Query("""
            SELECT b
            FROM Book b
            JOIN b.likes l
            WHERE l.member.id = :memberId
            ORDER BY SIZE(l) DESC
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

    @Query("""
            SELECT b
            FROM Book b
            LEFT JOIN b.likes l
            LEFT JOIN b.reviews r ON r.member.id = :memberId
            WHERE r.member.id = :memberId
            ORDER BY SIZE(l) DESC
            """)
    Page<Book> findBooksOrderByLikesDesc(Pageable pageable, @Param("memberId") UUID memberId);


    @Query("""
            SELECT b
            FROM Book b
            LEFT JOIN b.reviews r ON r.member.id = :memberId
            WHERE r.member.id = :memberId
            GROUP BY b
            ORDER BY b.publishedDate DESC
            """)
    Page<Book> findBooksOrderByDateDesc(Pageable pageable, @Param("memberId") UUID memberId);

}