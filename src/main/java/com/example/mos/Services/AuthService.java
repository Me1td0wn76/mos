package com.example.mos.Services;

import com.example.mos.Models.Employee;
import com.example.mos.Repositories.EmployeeRepository;
import com.example.mos.dto.LoginRequest;
import com.example.mos.dto.LoginResponse;
import com.example.mos.exception.InvalidRequestException;
import com.example.mos.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 認証サービス
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final EmployeeRepository employeeRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    
    public LoginResponse login(LoginRequest request) {
        Employee employee = employeeRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new InvalidRequestException("Invalid credentials"));
        
        // BCryptPasswordEncoderを使用してパスワードを検証
        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            throw new InvalidRequestException("Invalid credentials");
        }
        
        if (!employee.getIsActive()) {
            throw new InvalidRequestException("Employee account is not active");
        }
        
        String token = jwtUtil.generateToken(
                employee.getEmployeeId(),
                employee.getLoginId(),
                employee.getStore().getStoreId(),
                employee.getRoleId()
        );
        
        return new LoginResponse(
                employee.getEmployeeId(),
                employee.getEmployeeName(),
                employee.getRoleId(),
                employee.getStore().getStoreId(),
                token
        );
    }
}
