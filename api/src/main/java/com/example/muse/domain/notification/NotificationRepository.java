package com.example.muse.domain.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    boolean existsByEventId(UUID eventId);
}
