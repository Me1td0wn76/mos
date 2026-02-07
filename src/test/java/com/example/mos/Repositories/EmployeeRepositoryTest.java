package com.example.mos.Repositories;

import com.example.mos.Models.Employee;
import com.example.mos.Models.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EmployeeRepository の統合テスト
 */
@DataJpaTest
@ActiveProfiles("test")
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private StoreRepository storeRepository;

    private Store testStore;

    @BeforeEach
    void setUp() {
        testStore = new Store();
        testStore.setStoreName("テスト店舗");
        testStore.setIsActive(true);
        testStore = storeRepository.save(testStore);
    }

    @Test
    @DisplayName("従業員の保存と取得")
    void testSaveAndFind() {
        // Arrange
        Employee employee = new Employee();
        employee.setStore(testStore);
        employee.setEmployeeName("山田太郎");
        employee.setLoginId("yamada");
        employee.setPassword("password123");
        employee.setRoleId(1);
        employee.setIsActive(true);

        // Act
        Employee savedEmployee = employeeRepository.save(employee);
        Optional<Employee> foundEmployee = employeeRepository.findById(savedEmployee.getEmployeeId());

        // Assert
        assertTrue(foundEmployee.isPresent());
        assertEquals("山田太郎", foundEmployee.get().getEmployeeName());
        assertEquals("yamada", foundEmployee.get().getLoginId());
        assertEquals(1, foundEmployee.get().getRoleId());
    }

    @Test
    @DisplayName("ログインIDで従業員を検索")
    void testFindByLoginId() {
        // Arrange
        Employee employee = new Employee();
        employee.setStore(testStore);
        employee.setEmployeeName("佐藤花子");
        employee.setLoginId("sato");
        employee.setPassword("password123");
        employee.setRoleId(2);
        employee.setIsActive(true);
        employeeRepository.save(employee);

        // Act
        Optional<Employee> foundEmployee = employeeRepository.findByLoginId("sato");

        // Assert
        assertTrue(foundEmployee.isPresent());
        assertEquals("佐藤花子", foundEmployee.get().getEmployeeName());
    }

    @Test
    @DisplayName("店舗IDで従業員一覧を取得")
    void testFindByStore() {
        // Arrange
        Employee employee1 = new Employee();
        employee1.setStore(testStore);
        employee1.setEmployeeName("従業員1");
        employee1.setLoginId("emp1");
        employee1.setPassword("password");
        employee1.setRoleId(1);
        employee1.setIsActive(true);
        employeeRepository.save(employee1);

        Employee employee2 = new Employee();
        employee2.setStore(testStore);
        employee2.setEmployeeName("従業員2");
        employee2.setLoginId("emp2");
        employee2.setPassword("password");
        employee2.setRoleId(2);
        employee2.setIsActive(true);
        employeeRepository.save(employee2);

        // Act
        List<Employee> employees = employeeRepository.findByStore(testStore);

        // Assert
        assertEquals(2, employees.size());
    }

    @Test
    @DisplayName("アクティブな従業員のみ取得")
    void testFindByStoreAndIsActive() {
        // Arrange
        Employee activeEmployee = new Employee();
        activeEmployee.setStore(testStore);
        activeEmployee.setEmployeeName("アクティブ");
        activeEmployee.setLoginId("active");
        activeEmployee.setPassword("password");
        activeEmployee.setRoleId(1);
        activeEmployee.setIsActive(true);
        employeeRepository.save(activeEmployee);

        Employee inactiveEmployee = new Employee();
        inactiveEmployee.setStore(testStore);
        inactiveEmployee.setEmployeeName("非アクティブ");
        inactiveEmployee.setLoginId("inactive");
        inactiveEmployee.setPassword("password");
        inactiveEmployee.setRoleId(2);
        inactiveEmployee.setIsActive(false);
        employeeRepository.save(inactiveEmployee);

        // Act
        List<Employee> activeEmployees = employeeRepository.findByStoreAndIsActive(testStore, true);

        // Assert
        assertEquals(1, activeEmployees.size());
        assertTrue(activeEmployees.stream().allMatch(Employee::getIsActive));
    }
}
