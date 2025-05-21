package com.example.muse.domain.book;

import com.example.muse.domain.book.dto.SearchBookResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    public List<SearchBookResponseDto> searchBook(String title) {

        String normalizedTitle = title.toLowerCase().replace(" ", "");

        return bookRepository.findByTitleNormalizedContaining(normalizedTitle).stream()
                .map(SearchBookResponseDto::from)
                .toList();

    }

    public Book findById(Long bookId) {

        return bookRepository.findById(bookId).orElseThrow(IllegalArgumentException::new);
    }
}
