package com.example.mos.dto;

import lombok.Data;

import java.util.List;

/**
 * 注文作成リクエストDTO
 */
@Data
public class OrderCreateRequest {
    private String qrCode; // QRコードからの卓番号取得用（後方互換性のため残す）
    private Long storeId; // 店舗ID
    private String tableNumber; // テーブル番号（A01, B02など）
    private String planType; // NORMAL, ALL_YOU_CAN_DRINK_2H, ALL_YOU_CAN_DRINK_3H
    private Integer planDuration; // プラン時間（分）
    private List<OrderItemRequest> items;
}
