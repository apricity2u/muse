package com.example.muse.domain.book;

import com.example.muse.domain.book.dto.BookDto;
import com.example.muse.domain.book.dto.GetBookResponseDto;
import com.example.muse.domain.book.dto.GetBooksResponseDto;
import com.example.muse.domain.book.dto.SearchBookResponseDto;
import com.example.muse.domain.like.LikesService;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.review.ReviewService;
import com.example.muse.global.common.exception.CustomNotFoundException;
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

        return bookRepository.findById(bookId).orElseThrow(CustomNotFoundException::new);
    }

    @Transactional
    public void bookLike(Long bookId, Member member) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(CustomNotFoundException::new);

        likesService.createLike(book, member);
    }

    @Transactional
    public void bookUnlike(Long bookId, Member member) {

        likesService.unLikeBook(bookId, member);
    }

    public GetBookResponseDto getBook(Long bookId) {

        Book book = bookRepository.findById(bookId).orElseThrow(CustomNotFoundException::new);
        return GetBookResponseDto.from(book);
    }

    public GetBooksResponseDto getUserBooks(Pageable pageable, UUID memberId, Member loggedInMember) {

        pageable = setBookDefaultSort(pageable);
        boolean isLikesSort = pageable.getSort().stream()
                .anyMatch(order -> order.getProperty().equals("likes"));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        Page<Book> bookPage = isLikesSort
                ? bookRepository.findBooksOrderByLikesDesc(pageable, memberId)
                : bookRepository.findBooksOrderByDateDesc(pageable, memberId);
        Page<BookDto> bookDtoPage = bookPage.map(book -> BookDto.from(book, loggedInMember));

        return GetBooksResponseDto.from(bookDtoPage);
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

    public GetBooksResponseDto getLikedBooks(Pageable pageable, Member member) {

        pageable = setBookDefaultSort(pageable);
        boolean isLikesSort = pageable.getSort().stream()
                .anyMatch(order -> order.getProperty().equals("likes"));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        Page<Book> bookPage = isLikesSort
                ? bookRepository.findLikedBooksOrderByLikesDesc(member.getId(), pageable)
                : bookRepository.findLikedBooksOrderByDateDesc(member.getId(), pageable);

        Page<BookDto> bookDtoPage = bookPage.map(book -> BookDto.from(book, member));

        return GetBooksResponseDto.from(bookDtoPage);
    }
}
