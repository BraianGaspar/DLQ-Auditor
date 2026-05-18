package com.auditor.dlq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class DlxAuditorApplication {
    public static void main(String[] args) {
        // Log das propriedades de ambiente antes de iniciar
        log.info("AWS_ACCESS_KEY configurada: {}", System.getenv("AWS_ACCESS_KEY") != null);
        log.info("AWS_SECRET_KEY configurada: {}", System.getenv("AWS_SECRET_KEY") != null);
        log.info("AWS_REGION configurada: {}", System.getenv("AWS_REGION"));
        
        SpringApplication.run(DlxAuditorApplication.class, args);
    }
}
