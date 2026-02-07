package com.example.mos.Services;

import com.example.mos.Models.Employee;
import com.example.mos.Repositories.EmployeeRepository;
import com.example.mos.dto.LoginRequest;
import com.example.mos.dto.LoginResponse;
import com.example.mos.exception.InvalidRequestException;
import com.example.mos.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 認証サービス
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final EmployeeRepository employeeRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for loginId: {}", request.getLoginId());
        
        Employee employee = employeeRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> {
                    log.warn("Login failed: User not found - {}", request.getLoginId());
                    return new InvalidRequestException("Invalid credentials");
                });
        
        // BCryptPasswordEncoderを使用してパスワードを検証
        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            log.warn("Login failed: Invalid password for user - {}", request.getLoginId());
            throw new InvalidRequestException("Invalid credentials");
        }
        
        if (!employee.getIsActive()) {
            log.warn("Login failed: Inactive account - {}", request.getLoginId());
            throw new InvalidRequestException("Employee account is not active");
        }
        
        // Storeを明示的に取得（LAZY対策）
        Long storeId = employee.getStore().getStoreId();
        
        String token = jwtUtil.generateToken(
                employee.getEmployeeId(),
                employee.getLoginId(),
                storeId,
                employee.getRoleId()
        );
        
        log.info("Login successful for user: {}", request.getLoginId());
        
        return new LoginResponse(
                employee.getEmployeeId(),
                employee.getEmployeeName(),
                employee.getRoleId(),
                storeId,
                token
        );
    }
}
