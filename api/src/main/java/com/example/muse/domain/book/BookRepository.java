package com.example.muse.domain.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleNormalizedContaining(String normalizedTitle);


}
