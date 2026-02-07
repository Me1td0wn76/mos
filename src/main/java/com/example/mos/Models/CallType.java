package com.example.mos.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * T15: 呼出し種類マスタテーブル
 */
@Entity
@Table(name = "call_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "call_type_id")
    private Long callTypeId;
    
    @Column(name = "type_name", nullable = false, length = 100)
    private String typeName; // 例: 従業員呼び出し、お会計、その他
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
