package com.example.mos.Repositories;

import com.example.mos.Models.CallType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallTypeRepository extends JpaRepository<CallType, Long> {
    List<CallType> findByIsActive(Boolean isActive);
}
