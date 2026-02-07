package com.example.mos.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * T01: 注文テーブル
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private TableInfo table;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false)
    private PlanType planType;
    
    @Column(name = "plan_duration") // 飲み放題の時間（分）
    private Integer planDuration;
    
    @Column(name = "timer_start_time")
    private LocalDateTime timerStartTime; // タイマー開始時刻
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.PENDING;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum PlanType {
        NORMAL,             // 通常プラン
        ALL_YOU_CAN_DRINK_2H, // 飲み放題2時間
        ALL_YOU_CAN_DRINK_3H  // 飲み放題3時間
    }
    
    public enum OrderStatus {
        PENDING,      // 保留中
        CONFIRMED,    // 確定
        COMPLETED,    // 完了
        CANCELLED     // キャンセル
    }
}
