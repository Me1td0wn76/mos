package com.example.mos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 従業員レスポンスDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private Long employeeId;
    private Long storeId;
    private String employeeName;
    private String loginId;
    private Integer roleId;
    private Boolean isActive;
}
