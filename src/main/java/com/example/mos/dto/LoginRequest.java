package com.example.mos.dto;

import lombok.Data;

/**
 * ログインリクエストDTO
 */
@Data
public class LoginRequest {
    private String loginId;
    private String password;
}
