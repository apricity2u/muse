package com.example.muse.domain.book;

import com.example.muse.domain.book.dto.SearchBookResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class BookServiceCacheTest {
    @Autowired
    private BookService bookService;
    @Autowired
    private CacheManager cacheManager;
    @SpyBean
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        cacheManager.getCacheNames()
                .forEach(name -> cacheManager.getCache(name).clear());
    }

    @Test
    void searchBookChar_캐시_사용() {
        // given
        String query = "a";
        String normalized = query.toLowerCase().replace(" ", "");
        List<Book> books = Arrays.asList(
                Book.builder().id(1L).title("a1").build(),
                Book.builder().id(2L).title("a2").build(),
                Book.builder().id(3L).title("b1").build()
        );

        doReturn(books).when(bookRepository).findByTitleSingleKeyword(normalized);

        // when
        SearchBookResponseDto result1 = bookService.searchBook(normalized);
        SearchBookResponseDto result2 = bookService.searchBook(normalized);

        // then
        verify(bookRepository, times(1)).findByTitleSingleKeyword(normalized);
        assertNotNull(cacheManager.getCache("searchBookChar").get(normalized));
    }

    @Test
    void searchBook_캐시_사용() {
        // given
        String query = "spring";
        String normalized = query.toLowerCase().replace(" ", "");
        List<Book> books = Arrays.asList(
                Book.builder().id(1L).title("Spring Boot in Action").build(),
                Book.builder().id(2L).title("Spring Security Guide").build(),
                Book.builder().id(3L).title("Spring Data JPA").build()
        );

        doReturn(books).when(bookRepository).findByTitleContaining(normalized);

        // when
        bookService.searchBook(normalized);
        bookService.searchBook(normalized);

        // then
        verify(bookRepository, times(1)).findByTitleContaining(normalized);
        assertNotNull(cacheManager.getCache("searchBook").get(normalized));
    }
}