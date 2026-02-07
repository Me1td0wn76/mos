package com.example.mos.Services;

import com.example.mos.Models.CallRequest;
import com.example.mos.Models.CallResponse;
import com.example.mos.Models.CallType;
import com.example.mos.Models.Employee;
import com.example.mos.Models.TableInfo;
import com.example.mos.Repositories.CallRequestRepository;
import com.example.mos.Repositories.CallResponseRepository;
import com.example.mos.Repositories.CallTypeRepository;
import com.example.mos.Repositories.EmployeeRepository;
import com.example.mos.Repositories.TableInfoRepository;
import com.example.mos.dto.NotificationMessage;
import com.example.mos.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 呼び出しサービス
 */
@Service
@RequiredArgsConstructor
public class CallService {
    
    private final CallRequestRepository callRequestRepository;
    private final CallResponseRepository callResponseRepository;
    private final CallTypeRepository callTypeRepository;
    private final TableInfoRepository tableInfoRepository;
    private final EmployeeRepository employeeRepository;
    private final SimpMessagingTemplate messagingTemplate;
    
    @Transactional
    public CallRequest createCallRequest(Long tableId, Long callTypeId, String remarks) {
        TableInfo table = tableInfoRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));
        
        CallType callType = callTypeRepository.findById(callTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Call type not found"));
        
        CallRequest callRequest = new CallRequest();
        callRequest.setTable(table);
        callRequest.setCallType(callType);
        callRequest.setRemarks(remarks);
        callRequest.setStatus(CallRequest.CallStatus.PENDING);
        
        callRequest = callRequestRepository.save(callRequest);
        
        // WebSocket通知: 新規呼び出し
        Long storeId = table.getStore().getStoreId();
        NotificationMessage notification = NotificationMessage.create(
                NotificationMessage.NotificationType.NEW_CALL,
                storeId,
                callRequest.getCallId(),
                "テーブル" + table.getTableNumber() + "から呼び出しがあります: " + callType.getTypeName(),
                callRequest
        );
        messagingTemplate.convertAndSend("/topic/store/" + storeId + "/calls", notification);
        
        return callRequest;
    }
    
    @Transactional(readOnly = true)
    public List<CallRequest> getPendingCallsByStore(Long storeId) {
        return callRequestRepository.findByStoreIdAndStatus(storeId, CallRequest.CallStatus.PENDING);
    }
    
    @Transactional
    public CallRequest respondToCall(Long callId, Long employeeId, String remarks) {
        CallRequest callRequest = callRequestRepository.findById(callId)
                .orElseThrow(() -> new ResourceNotFoundException("Call request not found"));
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        
        // 呼び出し対応レコード作成
        CallResponse callResponse = new CallResponse();
        callResponse.setCallRequest(callRequest);
        callResponse.setEmployee(employee);
        callResponse.setResponseTime(LocalDateTime.now());
        callResponse.setRemarks(remarks);
        
        callResponseRepository.save(callResponse);
        
        // 呼び出しステータスを完了に更新
        callRequest.setStatus(CallRequest.CallStatus.COMPLETED);
        callRequest = callRequestRepository.save(callRequest);
        
        // WebSocket通知: 呼び出し対応
        Long storeId = callRequest.getTable().getStore().getStoreId();
        NotificationMessage notification = NotificationMessage.create(
                NotificationMessage.NotificationType.CALL_RESPONSE,
                storeId,
                callRequest.getCallId(),
                employee.getEmployeeName() + "が呼び出しに対応しました",
                callRequest
        );
        messagingTemplate.convertAndSend("/topic/store/" + storeId + "/calls", notification);
        
        return callRequest;
    }
}
