package com.example.muse.domain.book;

import com.example.muse.domain.book.dto.SearchBookResponseDto;
import com.example.muse.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/books/{bookId}/like")
    public void bookLike(
            @PathVariable Long bookId,
            @AuthenticationPrincipal Member member) {

        bookService.bookLike(bookId, member);
    }
}
