package com.example.muse.domain.outbox;

import com.example.muse.domain.book.Book;
import com.example.muse.domain.book.BookRepository;
import com.example.muse.domain.like.LikesService;
import com.example.muse.domain.member.Member;
import com.example.muse.domain.member.MemberRepository;
import com.example.muse.domain.notification.Notification;
import com.example.muse.domain.notification.NotificationRepository;
import com.example.muse.domain.review.Review;
import com.example.muse.domain.review.ReviewRepository;
import com.example.muse.integration.AbstractIntegrationTest;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
class OutboxEventServiceTest extends AbstractIntegrationTest {
    @Autowired
    LikesService likesService;
    @Autowired
    OutBoxEventRepository outBoxEventRepository;
    @Autowired
    OutboxEventService outboxEventService;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private Member member;
    private Review review;
    private Book book;

    @BeforeEach
    void setUp() {
        member = Member.builder().nickname("test").build();
        memberRepository.save(member);

        book = Book.builder().title("test").build();
        bookRepository.save(book);

        review = Review.builder().content("test").member(member).book(book).build();
        reviewRepository.save(review);
    }

    @Test
    void 좋아요하면_outbox에_쌓이고_퍼블리셔가_발송한다() {

        likesService.createReviewLike(review.getId(), member.getId());
        outboxEventService.publishUnpublishedEvents(10);

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        Awaitility.await().atMost(Duration.ofSeconds(10)).pollInterval(Duration.ofMillis(200))
                .untilAsserted(() ->
                        transactionTemplate.executeWithoutResult(status -> {
                                    assertThat(outBoxEventRepository.findByPublishedIsFalse(PageRequest.of(0, 100)))
                                            .isEmpty();
                                }
                        )
                );
    }

    @Test
    void 실제_rabbit큐에_메시지가_들어간다() {

        likesService.createReviewLike(review.getId(), member.getId());

        outboxEventService.publishUnpublishedEvents(10);

        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .pollInterval(Duration.ofMillis(100))
                .untilAsserted(() -> {
                    List<Notification> notifications = notificationRepository.findAll();

                    assertThat(notifications)
                            .isNotEmpty()
                            .anyMatch(n -> n.getType().equals("review.like")
                                    && n.getReviewId().equals(review.getId()));
                });
    }
}