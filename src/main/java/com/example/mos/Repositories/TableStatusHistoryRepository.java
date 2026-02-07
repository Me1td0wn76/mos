package com.example.mos.Repositories;

import com.example.mos.Models.TableInfo;
import com.example.mos.Models.TableStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableStatusHistoryRepository extends JpaRepository<TableStatusHistory, Long> {
    List<TableStatusHistory> findByTable(TableInfo table);
    List<TableStatusHistory> findByTableOrderByChangedAtDesc(TableInfo table);
}
