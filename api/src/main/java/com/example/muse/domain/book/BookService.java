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
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private static final String DAILY_KEY_PREFIX = "trending:";
    private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final BookRepository bookRepository;
    private final LikesService likesService;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private ZSetOperations<String, String> zOps;

    @PostConstruct
    private void init() {
        zOps = redisTemplate.opsForZSet();
    }

    @Caching(cacheable = {
            @Cacheable(value = "searchBook", key = "#title", condition = "#title.length() > 1"),
            @Cacheable(value = "searchBookChar", key = "#title", condition = "#title.length() == 1")
    })
    public SearchBookResponseDto searchBook(String title) {

        String normalizedTitle = title.toLowerCase().replace(" ", "");
        List<Book> books = title.length() == 1
                ? bookRepository.findByTitleSingleKeyword(normalizedTitle)
                : bookRepository.findByTitleContaining(normalizedTitle);

        return SearchBookResponseDto.from(books);
    }

    protected void increaseTrendingCount(Book book) {

        String todayKey = DAILY_KEY_PREFIX + LocalDate.now(ZONE).format(DATE_FORMATTER);

        zOps.incrementScore(todayKey, book.getId().toString(), 1.0);
        long ttl = redisTemplate.getExpire(todayKey);

        if (ttl == -1) {
            redisTemplate.expire(todayKey, Duration.ofDays(8));
        }
    }


    @Transactional
    public void bookLike(Long bookId, UUID memberId) {

        likesService.createBookLike(bookId, memberId);
    }

    @Transactional
    public void bookUnlike(Long bookId, UUID memberId) {

        likesService.unLikeBook(bookId, memberId);
    }

    public GetBookResponseDto getBook(Long bookId, UUID memberId) {

        Book book = bookRepository.findById(bookId).orElseThrow(CustomNotFoundException::new);
        Member member = memberId == null ? null : memberRepository.getReferenceById(memberId);

        increaseTrendingCount(book);

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

    @Cacheable("trendingBooks")
    public SearchBookResponseDto getTrendingBooks() {

        LocalDate today = LocalDate.now(ZONE);
        String todayKey = DAILY_KEY_PREFIX + today.format(DATE_FORMATTER);
        List<String> otherKeys = IntStream.range(1, 7)
                .mapToObj(i -> DAILY_KEY_PREFIX + today.minusDays(i).format(DATE_FORMATTER))
                .toList();

        String unionKey = DAILY_KEY_PREFIX + "7days:";
        zOps.unionAndStore(todayKey, otherKeys, unionKey);
        redisTemplate.expire(unionKey, Duration.ofSeconds(10));
        Set<ZSetOperations.TypedTuple<String>> tuples = zOps.reverseRangeWithScores(unionKey, 0, 9);

        if (tuples.isEmpty()) {
            return new SearchBookResponseDto();
        }

        List<Long> trendingBookIds = tuples.stream()
                .map(tuple -> Long.parseLong(tuple.getValue()))
                .toList();

        List<Book> trendingBooksData = bookRepository.findAllById(trendingBookIds);
        Map<Long, Book> trendingBookRank = trendingBooksData.stream()
                .collect(Collectors.toMap(Book::getId, Function.identity()));

        List<Book> trendingBooks = trendingBookIds.stream()
                .map(trendingBookRank::get)
                .toList();

        return SearchBookResponseDto.from(trendingBooks);
    }
}
