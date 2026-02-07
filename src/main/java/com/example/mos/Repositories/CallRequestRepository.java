package com.example.mos.Repositories;

import com.example.mos.Models.CallRequest;
import com.example.mos.Models.TableInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallRequestRepository extends JpaRepository<CallRequest, Long> {
    List<CallRequest> findByTable(TableInfo table);
    List<CallRequest> findByStatus(CallRequest.CallStatus status);
    
    @Query("SELECT c FROM CallRequest c WHERE c.table.store.storeId = :storeId AND c.status = :status")
    List<CallRequest> findByStoreIdAndStatus(@Param("storeId") Long storeId, @Param("status") CallRequest.CallStatus status);
}
