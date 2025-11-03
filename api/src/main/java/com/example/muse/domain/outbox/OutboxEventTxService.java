package com.example.muse.domain.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxEventTxService {
    private final OutBoxEventRepository outBoxEventRepository;

    @Transactional
    public void markEventAsPublished(Long eventId) {
        outBoxEventRepository.findById(eventId).ifPresent(e -> {
            e.setPublished(true);
            outBoxEventRepository.save(e);
        });
    }

    @Transactional
    public List<OutBoxEvent> fetchUnpublishedEvents(int page, int pageSize) {

        return outBoxEventRepository.findByPublishedIsFalse(PageRequest.of(page, pageSize));
    }
}
