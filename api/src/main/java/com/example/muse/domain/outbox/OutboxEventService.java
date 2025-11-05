package com.example.muse.domain.outbox;

import com.example.muse.global.messaging.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxEventService {
    private final OutboxEventTxService txService;
    private final RabbitTemplate rabbitTemplate;


    public void publishUnpublishedEvents(int pageSize) {
        int page = 0;

        while (true) {
            List<OutBoxEvent> events = txService.fetchUnpublishedEvents(page, pageSize);

            if (events == null || events.isEmpty()) {
                break;
            }

            events.forEach(event -> {
                try {
                    rabbitTemplate.convertAndSend(
                            RabbitConfig.EXCHANGE_NAME,
                            event.getType(),
                            event.getPayload());

                    txService.markEventAsPublished(event.getId());
                } catch (Exception e) {
                    log.error("Failed to publish event {}", event.getId());
                }
            });
            page++;
        }
    }
}
