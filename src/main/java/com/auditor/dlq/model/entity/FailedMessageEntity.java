package com.auditor.dlq.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "failed_message_audit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailedMessageEntity {
    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID errorId;
    
    @Column(nullable = false)
    private String queueName;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;
    
    @Column(nullable = false)
    private Instant timestamp;
    
    @Column(nullable = false)
    private String status;
    
    @Column(nullable = false)
    private String severity;
    
    @PrePersist
    protected void onCreate() {
        if (errorId == null) {
            errorId = UUID.randomUUID();
        }
        if (timestamp == null) {
            timestamp = Instant.now();
        }
        if (status == null) {
            status = "PENDING_ANALYSIS";
        }
    }
}
