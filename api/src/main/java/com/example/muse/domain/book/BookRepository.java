package com.example.muse.domain.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleNormalizedContaining(String normalizedTitle);


    Page<Book> findByReviewsMemberId(Pageable pageable, UUID memberId);
}
