package com.auditor.dlq;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class DlxAuditorApplication {
    public static void main(String[] args) {
        try {
            Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .ignoreIfMissing()
                .load();
            
            dotenv.entries().forEach(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();
                
                if ((key.equals("SQS_DLQ_NAME") || key.equals("SQS_QUEUE_NAME")) && value.endsWith(".fifo")) {
                    value = value.substring(0, value.length() - 5);
                }
                
                System.setProperty(key, value);
                System.setProperty(key.toLowerCase().replace("_", "."), value);
            });
            
        } catch (Exception e) {
            log.warn("Arquivo .env não encontrado, usando variáveis de ambiente do sistema");
        }
        
        SpringApplication.run(DlxAuditorApplication.class, args);
    }
}
