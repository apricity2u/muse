package com.example.muse.domain.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxEventScheduler {
    private final OutboxEventService outboxEventService;

    @Scheduled(fixedDelay = 1000 * 60)
    public void schedulePublishUnpublishedEvents() {

        outboxEventService.publishUnpublishedEvents(10);
    }
}
