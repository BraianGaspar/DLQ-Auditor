package com.auditor.dlq.listener;

import com.auditor.dlq.service.AuditService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DlxMessageListener {

    private final AuditService auditService;

    @SqsListener("${SQS_DLQ_NAME}")
    public void receiveMessage(String message) {
        try {
            log.info("Mensagem recebida da DLQ");
            log.debug("Payload: {}", message);
            
            auditService.processFailedMessage(message, "${SQS_DLQ_NAME}");
            
            log.info("Mensagem processada com sucesso");
            
        } catch (Exception e) {
            log.error("Erro ao processar mensagem da DLQ: {}", e.getMessage(), e);
            throw new RuntimeException("Falha no processamento", e);
        }
    }
}
