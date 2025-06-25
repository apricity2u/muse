package com.example.muse.domain.book;

import com.example.muse.domain.book.dto.GetBookResponseDto;
import com.example.muse.domain.book.dto.GetBooksResponseDto;
import com.example.muse.domain.book.dto.SearchBookResponseDto;
import com.example.muse.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
            @AuthenticationPrincipal UUID memberId) {

        bookService.bookLike(bookId, memberId);
        return ResponseEntity.ok().body(
                ApiResponse.ok("좋아요 성공", "SUCCESS", null)
        );
    }

    @DeleteMapping("/books/{bookId}/like")
    public ResponseEntity<ApiResponse<Void>> bookUnlike(
            @PathVariable Long bookId,
            @AuthenticationPrincipal UUID memberId) {

        bookService.bookUnlike(bookId, memberId);
        return ResponseEntity.ok().body(
                ApiResponse.ok("좋아요 취소 성공", "SUCCESS", null));
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<ApiResponse<GetBookResponseDto>> getBook(
            @PathVariable Long bookId) {


        return ResponseEntity.ok().body(
                ApiResponse.ok(
                        "도서 조회 성공", "SUCCESS", bookService.getBook(bookId)
                )
        );
    }

    @GetMapping("/users/{memberId}/books")
    public ResponseEntity<ApiResponse<GetBooksResponseDto>> getUserBooks(
            @PathVariable UUID memberId,
            @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = "createdAt") Pageable pageable,
            @AuthenticationPrincipal UUID authMemberId) {

        return ResponseEntity.ok().body(
                ApiResponse.ok(
                        "유저의 도서 목록 조회 성공",
                        "SUCCESS",
                        bookService.getUserBooks(pageable, memberId, authMemberId)
                )
        );
    }

    @GetMapping("/books/likes")
    public ResponseEntity<ApiResponse<GetBooksResponseDto>> getLikedBooks(
            @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = "createdAt") Pageable pageable,
            @AuthenticationPrincipal UUID memberId) {

        return ResponseEntity.ok().body(
                ApiResponse.ok(
                        "좋아요한 도서 목록 조회 성공",
                        "SUCCESS",
                        bookService.getLikedBooks(pageable, memberId)
                )
        );
    }
}
