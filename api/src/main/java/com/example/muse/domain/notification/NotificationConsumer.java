package com.example.muse.domain.notification;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @RabbitListener(queues = "notifications_queue", containerFactory = "rabbitListenerContainerFactory")
    public void handleNotificationMessage(Message message, Channel channel) {

        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        try {
            JsonNode node = objectMapper.readTree(body);
            UUID eventId = UUID.fromString(node.get("eventId").asText());
            String type = node.get("type").asText();
            JsonNode payload = node.get("payload");

            UUID receiverId = UUID.fromString(payload.get("receiverId").asText());
            UUID actorId = UUID.fromString(payload.get("actorId").asText());
            Long referenceId = payload.has("reviewId") ? payload.get("reviewId").asLong() : null;

            boolean created = notificationService.createIfNotExists(eventId, receiverId, actorId, type, referenceId, payload.toString());

            channel.basicAck(deliveryTag, false);

            if (created) {
                log.info("Notification created for event {}", eventId);
            } else {
                log.info("Notification already existed for event {}", eventId);
            }
        } catch (Exception e) {
            log.error("Failed to process notification message", e);
            channel.basicNack(deliveryTag, false, true);
        }
    }
}
