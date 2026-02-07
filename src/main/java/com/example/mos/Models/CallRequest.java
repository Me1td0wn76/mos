package com.example.mos.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * T13: 呼出しテーブル
 */
@Entity
@Table(name = "call_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "call_id")
    private Long callId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private TableInfo table;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "call_type_id", nullable = false)
    private CallType callType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CallStatus status = CallStatus.PENDING;
    
    @Column(name = "remarks", length = 500)
    private String remarks;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum CallStatus {
        PENDING,      // 未対応
        IN_PROGRESS,  // 対応中
        COMPLETED     // 対応完了
    }
}
