package com.example.muse.domain.notification;

import com.example.muse.domain.outbox.OutboxEventScheduler;
import com.example.muse.domain.outbox.OutboxEventService;
import com.example.muse.global.messaging.config.RabbitConfig;
import com.example.muse.integration.AbstractIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class NotificationConsumerTest extends AbstractIntegrationTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private OutboxEventScheduler outboxEventScheduler;
    @Autowired
    OutboxEventService outboxEventService;


    @BeforeEach
    void setUp() {
        notificationRepository.deleteAll();
    }

    @Test
    void 메시지받으면_notification이_생성된다() {

        UUID eventId = UUID.randomUUID();
        ObjectNode payload = objectMapper.createObjectNode()
                .put("actorId", String.valueOf(UUID.randomUUID()))
                .put("receiverId", String.valueOf(UUID.randomUUID()))
                .put("reviewId", 1);

        ObjectNode body = objectMapper.createObjectNode()
                .put("eventId", String.valueOf(eventId))
                .put("type", "review.like")
                .set("payload", payload);

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY_LIKE, body.toString());

        outboxEventService.publishUnpublishedEvents(10);

        Awaitility.await().atMost(Duration.ofSeconds(30)).untilAsserted(() ->
                assertTrue(notificationRepository.existsByEventId(eventId))
        );
    }
}
