package com.example.muse.domain.notification;

import com.example.muse.global.messaging.config.RabbitConfig;
import com.example.muse.intergration.AbstractIntegrationTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
class NotificationConsumerTest extends AbstractIntegrationTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ObjectMapper objectMapper;

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

        JsonNode body = objectMapper.createObjectNode()
                .put("eventId", String.valueOf(eventId))
                .put("type", "review.like")
                .set("payload", payload);

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY_LIKE, body.toString());
    }
}