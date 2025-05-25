package com.example.muse.domain.book;

import com.example.muse.domain.book.dto.BookDto;
import com.example.muse.domain.book.dto.GetBookResponseDto;
import com.example.muse.domain.book.dto.GetBooksResponseDto;
import com.example.muse.domain.book.dto.SearchBookResponseDto;
import com.example.muse.domain.like.LikesService;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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

    public GetBooksResponseDto getUserBooks(Pageable pageable, UUID memberId, Member loggedInMember) {

        pageable = setBookDefaultSort(pageable);
        Page<Book> books = bookRepository.findByReviewsMemberId(pageable, memberId);
        Page<BookDto> bookDtos = books.map(book -> BookDto.from(book, loggedInMember));

        return GetBooksResponseDto.from(bookDtos);
    }


    private static Pageable setBookDefaultSort(Pageable pageable) {

        Sort sort = pageable.getSort();
        boolean validSort = sort.stream()
                .allMatch(order -> ReviewService.ALLOWED_SORTS.contains(order.getProperty()));

        if (!validSort || sort.stream().anyMatch(order -> order.getProperty().equals("createdAt"))) {
            sort = Sort.by(Sort.Direction.DESC, "publishedDate");
        }

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }
}
