package com.auditor.dlq.service;

import com.auditor.dlq.model.dto.OrderMessageDTO;

public interface AuditService {
    void processFailedMessage(String rawPayload, String queueName);
    String calculateSeverity(OrderMessageDTO message);
}
