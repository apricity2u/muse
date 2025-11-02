package com.example.muse.domain.outbox;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;

public interface OutBoxEventRepository extends JpaRepository<OutBoxEvent, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<OutBoxEvent> findByPublishedIsFalse(Pageable pageable);
}
