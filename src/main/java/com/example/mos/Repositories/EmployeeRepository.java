package com.example.mos.Repositories;

import com.example.mos.Models.Employee;
import com.example.mos.Models.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByLoginId(String loginId);
    List<Employee> findByStore(Store store);
    List<Employee> findByStoreAndIsActive(Store store, Boolean isActive);
}
