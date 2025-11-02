package com.example.muse.domain.notification;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JdbcTypeCode(Types.VARCHAR)
    @Column(columnDefinition = "CHAR(36)", nullable = false, unique = true)
    private UUID eventId;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private Long reviewId;
    @Column(nullable = false)
    private UUID actorId;
    @Column(nullable = false)
    private UUID receiverId;
    @Column(columnDefinition = "JSON")
    private String payload;
}
