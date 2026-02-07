package com.example.mos.dto;

import lombok.Data;

import java.util.List;

/**
 * 注文商品リクエストDTO
 */
@Data
public class OrderItemRequest {
    private Long productId;
    private Integer quantity;
    private String remarks;
    private List<Long> optionIds; // 商品オプションID
}
