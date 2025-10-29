package com.example.muse.domain.outbox;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutBoxEventRepository extends JpaRepository<OutBoxEvent, Long> {

    List<OutBoxEvent> findByPublishedIsFalse(Pageable pageable);
}
