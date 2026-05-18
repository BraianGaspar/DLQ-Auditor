package com.auditor.dlq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import java.net.URI;

@Configuration
public class SqsConfig {
    
    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        String accessKey = System.getProperty("AWS_ACCESS_KEY", System.getenv("AWS_ACCESS_KEY"));
        String secretKey = System.getProperty("AWS_SECRET_KEY", System.getenv("AWS_SECRET_KEY"));
        String region = System.getProperty("aws.region", System.getenv("AWS_REGION"));
        
        if (region == null) {
            region = "us-east-1";
        }
        
        var builder = SqsAsyncClient.builder()
            .region(Region.of(region));
        
        // Configurar credenciais se disponíveis
        if (accessKey != null && secretKey != null && !accessKey.isEmpty() && !secretKey.isEmpty()) {
            builder.credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            ));
        }
        
        // Configurar endpoint customizado se necessário (para LocalStack)
        String endpoint = System.getProperty("AWS_SQS_ENDPOINT", System.getenv("AWS_SQS_ENDPOINT"));
        if (endpoint != null && !endpoint.isEmpty()) {
            builder.endpointOverride(URI.create(endpoint));
        }
        
        return builder.build();
    }
}
