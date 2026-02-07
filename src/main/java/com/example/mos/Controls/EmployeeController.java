package com.example.mos.Controls;

import com.example.mos.Services.EmployeeService;
import com.example.mos.dto.ApiResponse;
import com.example.mos.dto.EmployeeRequest;
import com.example.mos.dto.EmployeeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 従業員管理コントローラー
 */
@Slf4j
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@CrossOrigin
public class EmployeeController {
    
    private final EmployeeService employeeService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getAllEmployees() {
        List<EmployeeResponse> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(ApiResponse.success(employees));
    }
    
    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getEmployeesByStore(@PathVariable Long storeId) {
        List<EmployeeResponse> employees = employeeService.getEmployeesByStore(storeId);
        return ResponseEntity.ok(ApiResponse.success(employees));
    }
    
    @GetMapping("/{employeeId}")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployeeById(@PathVariable Long employeeId) {
        EmployeeResponse employee = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok(ApiResponse.success(employee));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeResponse>> createEmployee(@RequestBody EmployeeRequest request) {
        log.info("Creating new employee: {}", request.getLoginId());
        EmployeeResponse employee = employeeService.createEmployee(request);
        return ResponseEntity.ok(ApiResponse.success("Employee created successfully", employee));
    }
    
    @PutMapping("/{employeeId}")
    public ResponseEntity<ApiResponse<EmployeeResponse>> updateEmployee(
            @PathVariable Long employeeId,
            @RequestBody EmployeeRequest request) {
        log.info("Updating employee: {}", employeeId);
        EmployeeResponse employee = employeeService.updateEmployee(employeeId, request);
        return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", employee));
    }
    
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long employeeId) {
        log.info("Deleting employee: {}", employeeId);
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok(ApiResponse.success("Employee deleted successfully", null));
    }
}
