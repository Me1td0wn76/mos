package com.example.mos.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * QRコード生成ユーティリティ
 */
@Component
public class QRCodeUtil {
    
    /**
     * QRコードを生成してBase64形式で返す
     * @param data QRコードに埋め込むデータ
     * @param width QRコードの幅
     * @param height QRコードの高さ
     * @return Base64エンコードされたQRコード画像
     */
    public String generateQRCode(String data, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        
        byte[] qrCodeBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(qrCodeBytes);
    }
    
    /**
     * テーブル用のQRコードデータを生成
     * @param storeId 店舗ID
     * @param tableId テーブルID
     * @return QRコードに埋め込むデータ
     */
    public String generateTableQRData(Long storeId, Long tableId) {
        return String.format("MOS:STORE:%d:TABLE:%d", storeId, tableId);
    }
}
