package com.example.muse.domain.book;

import com.example.muse.domain.book.dto.GetBookResponseDto;
import com.example.muse.domain.book.dto.SearchBookResponseDto;
import com.example.muse.domain.member.Member;
import com.example.muse.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<Void>> bookLike(
            @PathVariable Long bookId,
            @AuthenticationPrincipal Member member) {

        bookService.bookLike(bookId, member);
        return ResponseEntity.ok().body(
                ApiResponse.ok("좋아요 성공", "SUCCESS", null)
        );
    }

    @DeleteMapping("/books/{bookId}/like")
    public ResponseEntity<ApiResponse<Void>> bookUnlike(
            @PathVariable Long bookId,
            @AuthenticationPrincipal Member member) {

        bookService.bookUnlike(bookId, member);
        return ResponseEntity.ok().body(
                ApiResponse.ok("좋아요 취소 성공", "SUCCESS", null));
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<ApiResponse<GetBookResponseDto>> GetBook(
            @PathVariable Long bookId) {


        return ResponseEntity.ok().body(
                ApiResponse.ok(
                        "도서 조회 성공", "SUCCESS", bookService.getBook(bookId)
                )
        );
    }
}
