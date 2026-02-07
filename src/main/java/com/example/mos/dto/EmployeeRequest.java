package com.example.mos.dto;

import lombok.Data;

/**
 * 従業員作成・更新リクエストDTO
 */
@Data
public class EmployeeRequest {
    private Long storeId;
    private String employeeName;
    private String loginId;
    private String password;
    private Integer roleId;
    private Boolean isActive;
}
