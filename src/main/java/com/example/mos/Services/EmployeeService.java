package com.example.mos.Services;

import com.example.mos.Models.Employee;
import com.example.mos.Models.Store;
import com.example.mos.Repositories.EmployeeRepository;
import com.example.mos.Repositories.StoreRepository;
import com.example.mos.dto.EmployeeRequest;
import com.example.mos.dto.EmployeeResponse;
import com.example.mos.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 従業員管理サービス
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getEmployeesByStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
        return employeeRepository.findByStore(store).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return toResponse(employee);
    }
    
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
        
        Employee employee = new Employee();
        employee.setStore(store);
        employee.setEmployeeName(request.getEmployeeName());
        employee.setLoginId(request.getLoginId());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setRoleId(request.getRoleId());
        employee.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        
        employee = employeeRepository.save(employee);
        log.info("Created new employee: {}", employee.getLoginId());
        return toResponse(employee);
    }
    
    @Transactional
    public EmployeeResponse updateEmployee(Long employeeId, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        
        if (request.getStoreId() != null) {
            Store store = storeRepository.findById(request.getStoreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
            employee.setStore(store);
        }
        
        if (request.getEmployeeName() != null) {
            employee.setEmployeeName(request.getEmployeeName());
        }
        
        if (request.getLoginId() != null) {
            employee.setLoginId(request.getLoginId());
        }
        
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            employee.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        if (request.getRoleId() != null) {
            employee.setRoleId(request.getRoleId());
        }
        
        if (request.getIsActive() != null) {
            employee.setIsActive(request.getIsActive());
        }
        
        employee = employeeRepository.save(employee);
        log.info("Updated employee: {}", employee.getLoginId());
        return toResponse(employee);
    }
    
    @Transactional
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        
        employeeRepository.delete(employee);
        log.info("Deleted employee: {}", employee.getLoginId());
    }
    
    private EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getEmployeeId(),
                employee.getStore().getStoreId(),
                employee.getEmployeeName(),
                employee.getLoginId(),
                employee.getRoleId(),
                employee.getIsActive()
        );
    }
}
