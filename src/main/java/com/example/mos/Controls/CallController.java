package com.example.mos.Controls;

import com.example.mos.Models.CallRequest;
import com.example.mos.Services.CallService;
import com.example.mos.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 呼び出しコントローラー
 */
@RestController
@RequestMapping("/api/calls")
@RequiredArgsConstructor
@CrossOrigin
public class CallController {
    
    private final CallService callService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<CallRequest>> createCallRequest(
            @RequestParam Long tableId,
            @RequestParam Long callTypeId,
            @RequestParam(required = false) String remarks) {
        CallRequest callRequest = callService.createCallRequest(tableId, callTypeId, remarks);
        return ResponseEntity.ok(ApiResponse.success("Call request created", callRequest));
    }
    
    @GetMapping("/store/{storeId}/pending")
    public ResponseEntity<ApiResponse<List<CallRequest>>> getPendingCallsByStore(@PathVariable Long storeId) {
        List<CallRequest> calls = callService.getPendingCallsByStore(storeId);
        return ResponseEntity.ok(ApiResponse.success(calls));
    }
    
    @PostMapping("/{callId}/respond")
    public ResponseEntity<ApiResponse<CallRequest>> respondToCall(
            @PathVariable Long callId,
            @RequestParam Long employeeId,
            @RequestParam(required = false) String remarks) {
        CallRequest callRequest = callService.respondToCall(callId, employeeId, remarks);
        return ResponseEntity.ok(ApiResponse.success("Call request responded", callRequest));
    }
}
