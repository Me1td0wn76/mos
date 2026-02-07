package com.example.mos.dto;

import lombok.Data;

/**
 * QRコード生成リクエストDTO
 */
@Data
public class QRCodeRequest {
    private Long tableId;
    private Integer quantity = 1; // 発行枚数（1-5）
}
