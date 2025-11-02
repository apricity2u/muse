package com.example.muse.domain.like;

import com.example.muse.domain.book.Book;
import com.example.muse.domain.book.BookRepository;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.member.MemberRepository;
import com.example.muse.intergration.AbstractIntegrationTest;
import lombok.extern.slf4j.Slf4j;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class LikeServiceTest extends AbstractIntegrationTest {
    @Autowired
    private LikesService likesService;
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BookRepository bookRepository;

    private Book testBook;
    private List<Member> testMembers = new ArrayList<>();

    @BeforeEach
    void setUp() {
        testBook = bookRepository.save(Book.builder().title("테스트 책").build());
        for (int i = 0; i < 50; i++) testMembers.add(memberRepository.save(Member.builder().nickname("m" + i).build()));
    }


    @Test
    void 같은유저_동시요청해도_하나만_생성된다() throws InterruptedException {
        UUID memberId = testMembers.get(0).getId();
        Long bookId = testBook.getId();
        int threads = 20;
        ExecutorService ex = Executors.newFixedThreadPool(threads);
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            ex.submit(() -> {
                try {
                    ready.countDown();
                    start.await();
                    likesService.createBookLike(bookId, memberId);
                } catch (Exception ignored) {
                } finally {
                    done.countDown();
                }
            });
        }
        assertTrue(ready.await(5, TimeUnit.SECONDS));
        start.countDown();
        assertTrue(done.await(10, TimeUnit.SECONDS));
        ex.shutdown();

        assertEquals(1, likesRepository.countByBookIdAndMemberId(bookId, memberId));
    }
}