package com.auditor.dlq.service.impl;

import com.auditor.dlq.model.dto.OrderMessageDTO;
import com.auditor.dlq.model.entity.FailedMessageEntity;
import com.auditor.dlq.repository.FailedMessageRepository;
import com.auditor.dlq.service.AuditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    
    private final FailedMessageRepository repository;
    private final ObjectMapper objectMapper;
    
    @Override
    @Transactional
    public void processFailedMessage(String rawPayload, String queueName) {
        try {
            log.info("Processando mensagem falha da DLQ: {}", queueName);
            
            OrderMessageDTO message = objectMapper.readValue(rawPayload, OrderMessageDTO.class);
            
            String severity = calculateSeverity(message);
            
            FailedMessageEntity entity = new FailedMessageEntity();
            entity.setQueueName(queueName);
            entity.setPayload(rawPayload);
            entity.setTimestamp(Instant.now());
            entity.setStatus("PENDING_ANALYSIS");
            entity.setSeverity(severity);
            
            FailedMessageEntity saved = repository.save(entity);
            
            log.info("Mensagem salva com sucesso. ID: {}, Severidade: {}", saved.getErrorId(), severity);
            
        } catch (Exception e) {
            log.error("Erro ao processar mensagem da DLQ: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao processar mensagem", e);
        }
    }
    
    @Override
    public String calculateSeverity(OrderMessageDTO message) {
        if (message == null || message.getOrderItems() == null) {
            log.warn("Mensagem sem orderItems, definindo severidade como LOW");
            return "LOW";
        }
        
        int totalItems = message.getOrderItems().stream()
                .mapToInt(item -> item.getAmount() != null ? item.getAmount() : 0)
                .sum();
        
        log.debug("Total de itens calculado: {}", totalItems);
        
        if (totalItems > 100) {
            return "HIGH";
        } else if (totalItems >= 50) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
}
