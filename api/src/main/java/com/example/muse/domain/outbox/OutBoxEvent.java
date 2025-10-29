package com.example.muse.domain.outbox;

import com.example.muse.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutBoxEvent extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JdbcTypeCode(Types.VARCHAR)
    @Column(columnDefinition = "CHAR(36)", nullable = false)
    private UUID eventId;
    @Column(nullable = false)
    private String type;
    @Column(columnDefinition = "jsonb")
    private String payload;
    @Column(nullable = false)
    private boolean published = false;

    @Builder
    public OutBoxEvent(UUID eventId, String type, String payload, boolean published) {
        this.eventId = eventId;
        this.type = type;
        this.payload = payload;
        this.published = published;
    }

    public void markAsPublished() {
        published = true;
    }
}
