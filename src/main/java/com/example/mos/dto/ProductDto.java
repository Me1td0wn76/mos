package com.example.mos.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

/**
 * 商品レスポンスDTO
 */
@Data
public class ProductDto {
    private Long productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Boolean isAvailable;
    private Boolean isSoldOut;
    private Set<String> categoryNames;
}
