package com.example.mos.Services;

import com.example.mos.Models.Employee;
import com.example.mos.Models.Store;
import com.example.mos.Repositories.EmployeeRepository;
import com.example.mos.dto.LoginRequest;
import com.example.mos.dto.LoginResponse;
import com.example.mos.exception.InvalidRequestException;
import com.example.mos.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * AuthService のユニットテスト
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private Employee testEmployee;
    private Store testStore;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        // テスト用店舗
        testStore = new Store();
        testStore.setStoreId(1L);
        testStore.setStoreName("テスト店舗");
        testStore.setIsActive(true);

        // テスト用従業員（BCryptハッシュ化されたパスワード）
        testEmployee = new Employee();
        testEmployee.setEmployeeId(1L);
        testEmployee.setEmployeeName("山田太郎");
        testEmployee.setLoginId("yamada");
        testEmployee.setPassword("$2a$10$N9qo8uLOickgx2ZMRZoMye1J8BcJjdQpNCOqYJRdAOiKEMg8I8jSG"); // BCryptハッシュ
        testEmployee.setRoleId(1);
        testEmployee.setIsActive(true);
        testEmployee.setStore(testStore);

        // ログインリクエスト
        loginRequest = new LoginRequest();
        loginRequest.setLoginId("yamada");
        loginRequest.setPassword("password123");
    }

    @Test
    @DisplayName("正常なログイン")
    void testLogin_Success() {
        // Arrange
        when(employeeRepository.findByLoginId("yamada"))
                .thenReturn(Optional.of(testEmployee));
        when(passwordEncoder.matches("password123", testEmployee.getPassword()))
                .thenReturn(true);
        when(jwtUtil.generateToken(any(), any(), any(), any()))
                .thenReturn("test-jwt-token");

        // Act
        LoginResponse response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getEmployeeId());
        assertEquals("山田太郎", response.getEmployeeName());
        assertEquals(1, response.getRoleId());
        assertEquals(1L, response.getStoreId());
        assertEquals("test-jwt-token", response.getToken());
    }

    @Test
    @DisplayName("存在しないログインID")
    void testLogin_InvalidLoginId() {
        // Arrange
        when(employeeRepository.findByLoginId("invalid"))
                .thenReturn(Optional.empty());

        LoginRequest invalidRequest = new LoginRequest();
        invalidRequest.setLoginId("invalid");
        invalidRequest.setPassword("password123");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            authService.login(invalidRequest);
        });
    }

    @Test
    @DisplayName("パスワード不一致")
    void testLogin_InvalidPassword() {
        // Arrange
        when(employeeRepository.findByLoginId("yamada"))
                .thenReturn(Optional.of(testEmployee));
        when(passwordEncoder.matches("wrongpassword", testEmployee.getPassword()))
                .thenReturn(false);

        LoginRequest wrongPasswordRequest = new LoginRequest();
        wrongPasswordRequest.setLoginId("yamada");
        wrongPasswordRequest.setPassword("wrongpassword");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            authService.login(wrongPasswordRequest);
        });
    }

    @Test
    @DisplayName("無効化された従業員アカウント")
    void testLogin_InactiveEmployee() {
        // Arrange
        testEmployee.setIsActive(false);
        when(employeeRepository.findByLoginId("yamada"))
                .thenReturn(Optional.of(testEmployee));
        when(passwordEncoder.matches("password123", testEmployee.getPassword()))
                .thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            authService.login(loginRequest);
        });
    }
}
