package com.example.mos.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JwtUtil のユニットテスト
 */
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "testSecretKeyForJwtTokenGenerationAndValidation2026TestSecureHS512Key");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L); // 24時間
    }

    @Test
    @DisplayName("JWTトークン生成")
    void testGenerateToken() {
        // Act
        String token = jwtUtil.generateToken(1L, "yamada", 1L, 1);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("トークンからログインIDを抽出")
    void testExtractLoginId() {
        // Arrange
        String token = jwtUtil.generateToken(1L, "yamada", 1L, 1);

        // Act
        String loginId = jwtUtil.extractLoginId(token);

        // Assert
        assertEquals("yamada", loginId);
    }

    @Test
    @DisplayName("トークンから従業員IDを抽出")
    void testExtractEmployeeId() {
        // Arrange
        String token = jwtUtil.generateToken(123L, "yamada", 1L, 1);

        // Act
        Long employeeId = jwtUtil.extractEmployeeId(token);

        // Assert
        assertEquals(123L, employeeId);
    }

    @Test
    @DisplayName("トークンから店舗IDを抽出")
    void testExtractStoreId() {
        // Arrange
        String token = jwtUtil.generateToken(1L, "yamada", 456L, 1);

        // Act
        Long storeId = jwtUtil.extractStoreId(token);

        // Assert
        assertEquals(456L, storeId);
    }

    @Test
    @DisplayName("トークンから役職IDを抽出")
    void testExtractRoleId() {
        // Arrange
        String token = jwtUtil.generateToken(1L, "yamada", 1L, 2);

        // Act
        Integer roleId = jwtUtil.extractRoleId(token);

        // Assert
        assertEquals(2, roleId);
    }

    @Test
    @DisplayName("有効なトークンの検証")
    void testValidateToken() {
        // Arrange
        String token = jwtUtil.generateToken(1L, "yamada", 1L, 1);

        // Act
        Boolean isValid = jwtUtil.validateToken(token, "yamada");

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("無効なログインIDでトークン検証")
    void testValidateToken_InvalidLoginId() {
        // Arrange
        String token = jwtUtil.generateToken(1L, "yamada", 1L, 1);

        // Act
        Boolean isValid = jwtUtil.validateToken(token, "wronguser");

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("トークンが期限切れでないことを確認")
    void testIsTokenNotExpired() {
        // Arrange
        String token = jwtUtil.generateToken(1L, "yamada", 1L, 1);

        // Act
        Boolean isExpired = jwtUtil.isTokenExpired(token);

        // Assert
        assertFalse(isExpired);
    }

    @Test
    @DisplayName("Claims全体を抽出")
    void testExtractClaims() {
        // Arrange
        String token = jwtUtil.generateToken(1L, "yamada", 1L, 1);

        // Act
        Claims claims = jwtUtil.extractClaims(token);

        // Assert
        assertNotNull(claims);
        assertEquals("yamada", claims.getSubject());
        assertEquals(1, claims.get("employeeId", Long.class));
        assertEquals(1, claims.get("storeId", Long.class));
        assertEquals(1, claims.get("roleId", Integer.class));
    }
}
