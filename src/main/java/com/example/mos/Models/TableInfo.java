package com.example.mos.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * T11: 卓テーブル
 */
@Entity
@Table(name = "tables")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableInfo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_id")
    private Long tableId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    
    @Column(name = "table_number", nullable = false, length = 10)
    private String tableNumber; // 例: A00, B01
    
    @Column(name = "floor", nullable = false)
    private Integer floor; // 1: 1階, 2: 2階
    
    @Column(name = "capacity", nullable = false)
    private Integer capacity; // 座席数
    
    @Column(name = "qr_code", unique = true, length = 255)
    private String qrCode; // QRコードデータ
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TableStatus status = TableStatus.AVAILABLE;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum TableStatus {
        AVAILABLE,      // 空席
        IN_USE,         // 使用中
        PAYMENT_DONE,   // 会計済
        OUT_OF_SERVICE  // 停止中
    }
}
