package com.example.muse.domain.book;

import com.example.muse.domain.book.dto.BookDto;
import com.example.muse.domain.book.dto.GetBookResponseDto;
import com.example.muse.domain.book.dto.GetBooksResponseDto;
import com.example.muse.domain.book.dto.SearchBookResponseDto;
import com.example.muse.domain.like.LikesService;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.member.MemberRepository;
import com.example.muse.domain.review.ReviewService;
import com.example.muse.global.common.exception.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    private final MemberRepository memberRepository;

    @Caching(cacheable = {
            @Cacheable(value = "searchBook", key = "#title", condition = "#title.length() > 1"),
            @Cacheable(value = "searchBookChar", key = "#title", condition = "#title.length() == 1")
    })
    public SearchBookResponseDto searchBook(String title) {

        String normalizedTitle = title.toLowerCase().replace(" ", "");
        List<Book> books;
        if (title.length() == 1) {
            books = bookRepository.findByTitleSingleKeyword(normalizedTitle);
        }
        books = bookRepository.findByTitleContaining(normalizedTitle);

        return SearchBookResponseDto.from(books);

    }


    @Transactional
    public void bookLike(Long bookId, UUID memberId) {


        Book book = bookRepository.getReferenceById(bookId);
        Member member = memberId == null ? null : memberRepository.getReferenceById(memberId);

        likesService.createLike(book, member);
    }

    @Transactional
    public void bookUnlike(Long bookId, UUID memberId) {

        likesService.unLikeBook(bookId, memberId);
    }

    public GetBookResponseDto getBook(Long bookId, UUID memberId) {

        Book book = bookRepository.findById(bookId).orElseThrow(CustomNotFoundException::new);
        Member member = memberId == null ? null : memberRepository.getReferenceById(memberId);

        return GetBookResponseDto.from(book, member);
    }

    public GetBooksResponseDto getUserBooks(Pageable pageable, UUID memberId, UUID authMemberId) {

        Member authMember = authMemberId == null ? null : memberRepository.getReferenceById(authMemberId);
        pageable = setBookDefaultSort(pageable);
        boolean isLikesSort = pageable.getSort().stream()
                .anyMatch(order -> order.getProperty().equals("likes"));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        Page<Book> bookPage = isLikesSort
                ? bookRepository.findBooksOrderByLikesDesc(pageable, memberId)
                : bookRepository.findBooksOrderByDateDesc(pageable, memberId);
        Page<BookDto> bookDtoPage = bookPage.map(book -> BookDto.from(book, authMember));

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

    public GetBooksResponseDto getLikedBooks(Pageable pageable, UUID memberId) {

        Member member = memberId == null ? null : memberRepository.getReferenceById(memberId);
        pageable = setBookDefaultSort(pageable);
        boolean isLikesSort = pageable.getSort().stream()
                .anyMatch(order -> order.getProperty().equals("likes"));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        Page<Book> bookPage = isLikesSort
                ? bookRepository.findLikedBooksOrderByLikesDesc(memberId, pageable)
                : bookRepository.findLikedBooksOrderByDateDesc(memberId, pageable);

        Page<BookDto> bookDtoPage = bookPage.map(book -> BookDto.from(book, member, true));

        return GetBooksResponseDto.from(bookDtoPage);
    }
}
