package com.example.mos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ログインレスポンスDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long employeeId;
    private String employeeName;
    private Integer roleId;
    private Long storeId;
    private String token;
}
