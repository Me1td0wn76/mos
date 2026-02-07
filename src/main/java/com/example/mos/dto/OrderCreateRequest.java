package com.example.mos.dto;

import lombok.Data;

import java.util.List;

/**
 * 注文作成リクエストDTO
 */
@Data
public class OrderCreateRequest {
    private String qrCode; // QRコードからの卓番号取得用
    private String planType; // NORMAL, ALL_YOU_CAN_DRINK_2H, ALL_YOU_CAN_DRINK_3H
    private Integer planDuration; // プラン時間（分）
    private List<OrderItemRequest> items;
}
