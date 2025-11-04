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
            FROM book b
            WHERE MATCH(b.title_normalized)
                  AGAINST(CONCAT('+', :query) IN BOOLEAN MODE)
            ORDER BY
                (b.title_normalized LIKE CONCAT(:query, '%')) DESC,
                MATCH(b.title_normalized)
                  AGAINST(:query IN NATURAL LANGUAGE MODE) DESC
            LIMIT 10;
            """, nativeQuery = true)
    List<Book> findByTitleContaining(@Param("query") String query);


    @Query("""
            SELECT b
            FROM Book b
            JOIN b.likes l
            WHERE l.member.id = :memberId
            GROUP BY b
            ORDER BY SIZE(l) DESC, b.id
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
            ORDER BY b.publishedDate DESC, b.id
            """)
    Page<Book> findLikedBooksOrderByDateDesc(
            @Param("memberId") UUID memberId,
            Pageable pageable
    );

    @Query("""
            SELECT b
            FROM Book b
            JOIN b.reviews r WITH r.member.id = :memberId
            GROUP BY b
            ORDER BY SIZE(b.likes) DESC, b.id
            """)
    Page<Book> findBooksOrderByLikesDesc(Pageable pageable, @Param("memberId") UUID memberId);


    @Query("""
            SELECT b
            FROM Book b
            JOIN b.reviews r WITH r.member.id = :memberId
            GROUP BY b
            ORDER BY b.publishedDate DESC, b.id
            """)
    Page<Book> findBooksOrderByDateDesc(Pageable pageable, @Param("memberId") UUID memberId);

    @Query(value = """
            SELECT *
            FROM book b
            WHERE b.title_normalized LIKE CONCAT('%', :normalizedTitle, '%')
            ORDER BY
                b.title_normalized LIKE CONCAT(:normalizedTitle, '%') DESC,
                b.title_normalized ASC,
                b.id
            LIMIT 10;
            """, nativeQuery = true)
    List<Book> findByTitleSingleKeyword(String normalizedTitle);
}