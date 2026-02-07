package com.example.mos.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * T06: 商品オプションテーブル
 */
@Entity
@Table(name = "product_options")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOption {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long optionId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "option_name", nullable = false, length = 100)
    private String optionName; // 例: たれの種類、トッピング
    
    @Column(name = "option_value", nullable = false, length = 100)
    private String optionValue; // 例: 塩、タレ、大盛り
    
    @Column(name = "additional_price", precision = 10, scale = 2)
    private BigDecimal additionalPrice = BigDecimal.ZERO;
    
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
