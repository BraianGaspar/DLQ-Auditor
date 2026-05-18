package com.auditor.dlq.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class EnvConfig {
    
    @PostConstruct
    public void loadEnv() {
        try {
            Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();
            
            dotenv.entries().forEach(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();
                System.setProperty(key, value);
                System.setProperty(key.replace("_", ".").toLowerCase(), value);
                log.debug("Set property: {} = {}", key, value);
            });
            
            // Garantir que a região está configurada
            String region = System.getProperty("aws.region");
            if (region == null) {
                region = System.getenv("AWS_REGION");
                if (region != null) {
                    System.setProperty("aws.region", region);
                }
            }
            
            log.info("✅ Arquivo .env carregado com sucesso!");
            log.info("AWS_REGION: {}", System.getProperty("AWS_REGION", "não definido"));
            log.info("aws.region: {}", System.getProperty("aws.region", "não definido"));
            log.info("SQS_DLQ_NAME: {}", System.getProperty("SQS_DLQ_NAME", "não definido"));
            
        } catch (Exception e) {
            log.warn("⚠️ Arquivo .env não encontrado, usando variáveis de ambiente do sistema");
        }
    }
}