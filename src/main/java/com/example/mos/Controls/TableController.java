package com.example.mos.Controls;

import com.example.mos.Models.TableInfo;
import com.example.mos.Services.TableService;
import com.example.mos.dto.ApiResponse;
import com.example.mos.dto.QRCodeRequest;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * テーブル管理コントローラー
 */
@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
@CrossOrigin
public class TableController {
    
    private final TableService tableService;
    
    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse<List<TableInfo>>> getTablesByStore(@PathVariable Long storeId) {
        List<TableInfo> tables = tableService.getTablesByStore(storeId);
        return ResponseEntity.ok(ApiResponse.success(tables));
    }
    
    @GetMapping("/store/{storeId}/floor/{floor}")
    public ResponseEntity<ApiResponse<List<TableInfo>>> getTablesByStoreAndFloor(
            @PathVariable Long storeId,
            @PathVariable Integer floor) {
        List<TableInfo> tables = tableService.getTablesByStoreAndFloor(storeId, floor);
        return ResponseEntity.ok(ApiResponse.success(tables));
    }
    
    @PatchMapping("/{tableId}/status")
    public ResponseEntity<ApiResponse<TableInfo>> updateTableStatus(
            @PathVariable Long tableId,
            @RequestParam String status) {
        TableInfo table = tableService.updateTableStatus(tableId, status);
        return ResponseEntity.ok(ApiResponse.success("Table status updated", table));
    }
    
    @PostMapping("/qr-codes")
    public ResponseEntity<ApiResponse<List<String>>> generateQRCodes(@RequestBody QRCodeRequest request) 
            throws WriterException, IOException {
        List<String> qrCodes = tableService.generateQRCodes(request);
        return ResponseEntity.ok(ApiResponse.success("QR codes generated successfully", qrCodes));
    }
}
