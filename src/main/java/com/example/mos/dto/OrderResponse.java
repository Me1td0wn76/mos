package com.example.mos.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 注文レスポンスDTO
 */
@Data
public class OrderResponse {
    private Long orderId;
    private String tableNumber;
    private String planType;
    private Integer planDuration;
    private LocalDateTime timerStartTime;
    private String status;
    private List<OrderDetailDto> details;
    private LocalDateTime createdAt;
    
    @Data
    public static class OrderDetailDto {
        private Long orderDetailId;
        private String productName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
        private String remarks;
        private String status;
        private List<String> options;
    }
}
