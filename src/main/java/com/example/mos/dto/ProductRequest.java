package com.example.mos.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

/**
 * 商品作成・更新リクエストDTO
 */
@Data
public class ProductRequest {
    private Long storeId;
    private String productName;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Boolean isAvailable;
    private Boolean isSoldOut;
    private Set<Long> categoryIds;
}
