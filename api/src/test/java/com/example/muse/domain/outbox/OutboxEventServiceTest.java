package com.example.muse.domain.outbox;

import com.example.muse.domain.like.LikesService;
import com.example.muse.domain.notification.NotificationRepository;
import com.example.muse.intergration.AbstractIntegrationTest;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OutboxEventServiceTest extends AbstractIntegrationTest {
    @Autowired
    LikesService likesService;
    @Autowired
    OutBoxEventRepository outBoxEventRepository;
    @Autowired
    OutboxEventService outboxEventService;
    @Autowired
    NotificationRepository notificationRepository;

    @Test
    void 좋아요하면_outbox에_쌓이고_퍼블리셔가_발송한다() {

        likesService.createBookLike(1L, UUID.randomUUID());
        outboxEventService.publishUnpublishedEvents(10);

        Awaitility.await().untilAsserted(() -> {
            assertThat(outBoxEventRepository.findByPublishedIsFalse(PageRequest.of(0, 10)).isEmpty());
        });
    }
}