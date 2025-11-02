package com.example.muse.domain.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    public boolean createIfNotExists(UUID eventId, UUID receiverId, UUID actorId, String type, Long reviewId, String payloadJson) {

        if (notificationRepository.existsByEventId(eventId)) {
            return false;
        }

        Notification notification = Notification.builder()
                .eventId(eventId)
                .receiverId(receiverId)
                .actorId(actorId)
                .type(type)
                .reviewId(reviewId)
                .payload(payloadJson)
                .build();

        notificationRepository.save(notification);


        Map<String, Object> push = Map.of(
                "eventId", eventId,
                "receiverId", receiverId,
                "actorId", actorId,
                "type", type,
                "reviewId", reviewId,
                "payload", payloadJson

        );

        try {
            simpMessagingTemplate.convertAndSendToUser(receiverId.toString(), "/queue/notifications", push);
        } catch (Exception ex) {
            log.warn("WebSocket push failed for user={}, event={}", receiverId, eventId, ex);
        }

        return true;
    }
}