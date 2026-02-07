package com.example.mos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * セキュリティ設定
 * パスワードハッシュ化用のBCryptPasswordEncoderを提供
 */
@Configuration
public class SecurityConfig {

    /**
     * パスワードエンコーダー
     * BCryptアルゴリズムを使用してパスワードをハッシュ化
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
