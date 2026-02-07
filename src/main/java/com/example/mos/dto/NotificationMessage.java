package com.example.mos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * WebSocket通知メッセージDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {
    
    /**
     * 通知タイプ
     */
    public enum NotificationType {
        NEW_ORDER,          // 新規注文
        ORDER_STATUS_UPDATE, // 注文ステータス更新
        NEW_CALL,           // 新規呼び出し
        CALL_RESPONSE,      // 呼び出し対応
        TABLE_STATUS_UPDATE // テーブルステータス更新
    }
    
    private NotificationType type;
    private Long storeId;
    private Long entityId;  // Order ID, Call ID, etc.
    private String message;
    private Object data;    // 追加データ（任意）
    private LocalDateTime timestamp;
    
    /**
     * 通知メッセージを作成
     */
    public static NotificationMessage create(NotificationType type, Long storeId, Long entityId, String message, Object data) {
        NotificationMessage notification = new NotificationMessage();
        notification.setType(type);
        notification.setStoreId(storeId);
        notification.setEntityId(entityId);
        notification.setMessage(message);
        notification.setData(data);
        notification.setTimestamp(LocalDateTime.now());
        return notification;
    }
}
