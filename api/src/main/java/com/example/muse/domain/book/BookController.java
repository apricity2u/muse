package com.example.muse.domain.book;

import com.example.muse.domain.book.dto.SearchBookResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookController {
    private final BookService bookService;

    @GetMapping("/books")
    public List<SearchBookResponseDto> searchBook(@RequestParam String title) {
        
        return bookService.searchBook(title);
    }
}
