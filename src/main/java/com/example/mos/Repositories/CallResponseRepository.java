package com.example.mos.Repositories;

import com.example.mos.Models.CallRequest;
import com.example.mos.Models.CallResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallResponseRepository extends JpaRepository<CallResponse, Long> {
    List<CallResponse> findByCallRequest(CallRequest callRequest);
}
