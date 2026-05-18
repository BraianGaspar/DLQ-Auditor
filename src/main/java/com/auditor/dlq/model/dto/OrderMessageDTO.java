package com.auditor.dlq.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessageDTO {
    private String zipCode;
    private Long customerId;
    private List<OrderItemDTO> orderItems;
    private String origin;
    private Instant occurredAt;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDTO {
        private Long sku;
        private Integer amount;
    }
}
