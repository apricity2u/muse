package com.example.muse.domain.book;

import com.example.muse.domain.book.dto.GetBookResponseDto;
import com.example.muse.domain.book.dto.SearchBookResponseDto;
import com.example.muse.domain.like.LikesService;
import com.example.muse.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final LikesService likesService;

    public List<SearchBookResponseDto> searchBook(String title) {

        String normalizedTitle = title.toLowerCase().replace(" ", "");

        return bookRepository.findByTitleNormalizedContaining(normalizedTitle).stream()
                .map(SearchBookResponseDto::from)
                .toList();

    }

    public Book findById(Long bookId) {

        return bookRepository.findById(bookId).orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public void bookLike(Long bookId, Member member) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다."));

        likesService.createLike(book, member);
    }

    @Transactional
    public void bookUnlike(Long bookId, Member member) {

        likesService.unLikeBook(bookId, member);
    }

    public GetBookResponseDto getBook(Long bookId) {

        Book book = bookRepository.findById(bookId).orElseThrow(IllegalArgumentException::new);
        return GetBookResponseDto.from(book);
    }

}
