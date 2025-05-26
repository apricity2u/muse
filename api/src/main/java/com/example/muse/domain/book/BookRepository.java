package com.example.muse.domain.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleNormalizedContaining(String normalizedTitle);


    Page<Book> findByReviewsMemberId(Pageable pageable, UUID memberId);

    @Query(value = """
            SELECT b.*
            FROM book b
            LEFT JOIN likes l ON b.id = l.book_id AND l.member_id = :memberId
            GROUP BY b.id
            ORDER BY COUNT(l.id) DESC
            LIMIT :limit OFFSET :offset;
                        """,
            nativeQuery = true
    )
    List<Book> findByLikesMemberId(UUID memberId, int limit, long offset);


    @Query(value = """
            SELECT COUNT(DISTINCT b.id)
            FROM book b
            LEFT JOIN likes l ON b.id = l.book_id
            WHERE l.member_id = :memberId
            """,
            nativeQuery = true
    )
    long countByLikesMemberId(UUID memberId);
}
