package com.example.muse.domain.notification;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
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
    public void handleNotificationMessage(Message message) {

        String body = new String(message.getBody(), StandardCharsets.UTF_8);

        try {
            JsonNode node = objectMapper.readTree(body);

            if (!node.hasNonNull("eventId") || !node.hasNonNull("type")) {
                log.warn("Invalid message (missing eventId/type). body={}", body);
                return;
            }

            UUID eventId = UUID.fromString(node.get("eventId").asText());
            String type = node.get("type").asText();
            JsonNode payloadNode = node.has("payload") ? node.get("payload") : node;

            if (payloadNode == null || !payloadNode.hasNonNull("receiverId") || !payloadNode.hasNonNull("actorId")) {
                log.warn("Missing receiverId/actorId in payload. eventId={}, body={}", eventId, body);
                return;
            }

            UUID receiverId = UUID.fromString(payloadNode.get("receiverId").asText());
            UUID actorId = UUID.fromString(payloadNode.get("actorId").asText());
            Long referenceId = payloadNode.hasNonNull("reviewId") ? payloadNode.get("reviewId").asLong() : null;

            boolean created = notificationService.createIfNotExists(
                    eventId, receiverId, actorId, type, referenceId, payloadNode.toString()
            );

            log.info(created ? "Notification created for event {}" : "Notification already existed for event {}",
                    eventId);

        } catch (IllegalArgumentException ex) {
            log.warn("Bad message content, dropping. body={}, cause={}", body, ex.getMessage());
        } catch (Exception e) {
            log.error("Failed to process notification message", e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
