package com.example.mos.Services;

import com.example.mos.Models.Store;
import com.example.mos.Models.TableInfo;
import com.example.mos.Repositories.StoreRepository;
import com.example.mos.Repositories.TableInfoRepository;
import com.example.mos.dto.QRCodeRequest;
import com.example.mos.exception.InvalidRequestException;
import com.example.mos.exception.ResourceNotFoundException;
import com.example.mos.util.QRCodeUtil;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * テーブル管理サービス
 */
@Service
@RequiredArgsConstructor
public class TableService {
    
    private final TableInfoRepository tableInfoRepository;
    private final StoreRepository storeRepository;
    private final QRCodeUtil qrCodeUtil;
    
    @Transactional(readOnly = true)
    public List<TableInfo> getTablesByStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
        return tableInfoRepository.findByStore(store);
    }
    
    @Transactional(readOnly = true)
    public List<TableInfo> getTablesByStoreAndFloor(Long storeId, Integer floor) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
        return tableInfoRepository.findByStoreAndFloor(store, floor);
    }
    
    @Transactional
    public TableInfo updateTableStatus(Long tableId, String status) {
        TableInfo table = tableInfoRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));
        
        table.setStatus(TableInfo.TableStatus.valueOf(status));
        return tableInfoRepository.save(table);
    }
    
    @Transactional
    public List<String> generateQRCodes(QRCodeRequest request) throws WriterException, IOException {
        TableInfo table = tableInfoRepository.findById(request.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));
        
        if (table.getStatus() != TableInfo.TableStatus.OUT_OF_SERVICE) {
            throw new InvalidRequestException("Can only generate QR codes for tables with OUT_OF_SERVICE status");
        }
        
        if (request.getQuantity() < 1 || request.getQuantity() > 5) {
            throw new InvalidRequestException("Quantity must be between 1 and 5");
        }
        
        List<String> qrCodes = new ArrayList<>();
        
        // QRコードデータを生成
        String qrData = qrCodeUtil.generateTableQRData(
                table.getStore().getStoreId(),
                table.getTableId()
        );
        
        // テーブルにQRコードデータを保存
        if (table.getQrCode() == null) {
            table.setQrCode(qrData);
            tableInfoRepository.save(table);
        }
        
        // 指定された枚数のQRコード画像を生成
        for (int i = 0; i < request.getQuantity(); i++) {
            String qrCodeImage = qrCodeUtil.generateQRCode(qrData, 300, 300);
            qrCodes.add(qrCodeImage);
        }
        
        return qrCodes;
    }
    
    @Transactional(readOnly = true)
    public TableInfo verifyTableNumber(Long storeId, String tableNumber) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
        return tableInfoRepository.findByStoreAndTableNumber(store, tableNumber)
                .orElseThrow(() -> new ResourceNotFoundException("テーブル番号「" + tableNumber + "」は存在しません"));
    }
}
