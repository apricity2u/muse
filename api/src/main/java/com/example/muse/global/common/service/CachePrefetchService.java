package com.example.muse.global.common.service;

import com.example.muse.domain.book.BookService;
import com.example.muse.domain.book.dto.SearchBookResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class CachePrefetchService {
    private final BookService bookService;
    private final CacheManager cacheManager;

    @EventListener(ApplicationReadyEvent.class)
    public void prefetchCache() {

        Cache cache = cacheManager.getCache("searchBookChar");

        for (char c = 'ㄱ'; c <= 'ㅎ'; c++) {
            String key = String.valueOf(c);
            if (cache.get(key) != null) {
                continue;
            }

            SearchBookResponseDto searchBookResponseDto = bookService.searchBook(key);
            cache.put(key, searchBookResponseDto);
        }
        for (char c = '가'; c <= '힣'; c++) {
            String key = String.valueOf(c);
            if (cache.get(key) != null) {
                continue;
            }

            SearchBookResponseDto searchBookResponseDto = bookService.searchBook(key);
            cache.put(key, searchBookResponseDto);
        }
        for (char c = 'a'; c <= 'z'; c++) {
            String key = String.valueOf(c);
            if (cache.get(key) != null) {
                continue;
            }

            SearchBookResponseDto searchBookResponseDto = bookService.searchBook(key);
            cache.put(key, searchBookResponseDto);
        }
        log.info("도서 검색 초기 캐싱 완료");
    }
}
