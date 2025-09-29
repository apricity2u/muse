package com.example.muse.domain.like;

import com.example.muse.domain.book.Book;
import com.example.muse.domain.book.BookRepository;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.member.MemberRepository;
import com.example.muse.global.common.exception.CustomBadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class LikesServiceTest {
    @Autowired
    private LikesService likesService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LikesRepository likesRepository;
    private Book testBook;
    private List<Member> testMembers = new ArrayList<>();
    private final int LIKE_COUNT = 500;

    @BeforeEach
    void setUp() {

        testBook = bookRepository.save(
                Book.builder()
                        .title("테스트 책")
                        .build()
        );

        for (int i = 0; i < LIKE_COUNT; i++) {
            testMembers.add(
                    Member.builder()
                            .nickname("test" + i)
                            .build()
            );
        }
        testMembers = memberRepository.saveAll(testMembers);
    }

    @Test
    void 잘못된_좋아요_취소하면_예외발생한다() {

        UUID memberId = testMembers.get(0).getId();
        long bookId = testBook.getId();

        assertThrows(CustomBadRequestException.class,
                () -> likesService.unLikeBook(bookId, memberId));
    }

    @Test
    void 정합성_및_성능_측정() throws InterruptedException {

        int tasks = LIKE_COUNT;
        long durationNs;
        try (ExecutorService executor = Executors.newFixedThreadPool(LIKE_COUNT)) {

            CountDownLatch readyLatch = new CountDownLatch(tasks);
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch doneLatch = new CountDownLatch(tasks);

            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger exceptions = new AtomicInteger(0);
            long startNs;

            for (Member member : testMembers) {
                executor.submit(() -> {
                    try {
                        readyLatch.countDown();
                        startLatch.await();
                        likesService.createBookLike(testBook.getId(), member.getId());
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        exceptions.incrementAndGet();
                        log.error("에러 발생", e);
                    } finally {
                        doneLatch.countDown();
                    }
                });
            }

            assertTrue(readyLatch.await(10, TimeUnit.SECONDS));

            startNs = System.nanoTime();
            startLatch.countDown();

            boolean finished = doneLatch.await(60, TimeUnit.SECONDS);
            durationNs = System.nanoTime() - startNs;
            executor.shutdown();

            if (!finished) {
                executor.shutdownNow();
                throw new IllegalStateException("타임아웃");
            }
        }

        long likeCount = likesRepository.countByBookId(testBook.getId());
        double durationSec = durationNs / 1_000_000_000.0;

        log.info("정합성 및 성능 측정 결과");
        log.info("DB 최종 좋아요 수: {}", likeCount);
        log.info("총 소요 시간: {}초", String.format("%.3f", durationSec));
        log.info("평균 응답 시간: {}초", String.format("%.6f", durationSec / LIKE_COUNT));

        assertEquals(LIKE_COUNT, likeCount, "모든 유저의 좋아요가 정상 반영되어야 합니다.");
    }

    @Test
    void 같은유저_동시요청해도_하나만_생성된다() throws InterruptedException {

        int threads = 50;
        UUID memberId = testMembers.get(0).getId();
        Long bookId = testBook.getId();

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch readyLatch = new CountDownLatch(threads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await();
                    likesService.createBookLike(bookId, memberId);
                } catch (Exception e) {
                    log.debug("중복 요청에서 예외 발생 (정상 동작일 수 있음): {}", e.getMessage());
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        assertTrue(readyLatch.await(10, TimeUnit.SECONDS));
        long beginMs = System.currentTimeMillis();
        startLatch.countDown();
        assertTrue(doneLatch.await(30, TimeUnit.SECONDS));
        long total = System.currentTimeMillis() - beginMs;
        executor.shutdown();

        long likeCount = likesRepository.countByBookIdAndMemberId(bookId, memberId);

        log.info("같은 유저 동시 요청 결과");
        log.info("DB 좋아요 최종 카운트: {}", likeCount);
        log.info("총 소요(ms): {}", total);

        assertEquals(1, likeCount, "같은 유저는 좋아요가 하나만 생성되어야 합니다.");
    }

    @Test
    void 도서좋아요_삭제된다() {

        UUID memberId = testMembers.get(0).getId();
        long bookId = testBook.getId();

        likesService.createBookLike(bookId, memberId);
        likesService.unLikeBook(bookId, memberId);

        long count = likesRepository.countByBookIdAndMemberId(bookId, memberId);
        assertEquals(0, count);
    }

    @AfterEach
    void cleanUp() {

        List<Likes> likesList = likesRepository.findAllByBookId(testBook.getId());
        likesRepository.deleteAllInBatch(likesList);

        List<Book> books = List.of(testBook);
        bookRepository.deleteAllInBatch(books);

        memberRepository.deleteAllInBatch(testMembers);

    }
}