package com.example.mos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket設定
 * リアルタイム通知のためのWebSocket通信を設定
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * メッセージブローカーの設定
     * - /topic: 1対多のブロードキャスト用
     * - /app: クライアントからサーバーへのメッセージ送信用プレフィックス
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // サーバーからクライアントへのメッセージ送信用プレフィックス
        config.enableSimpleBroker("/topic", "/queue");
        // クライアントからサーバーへのメッセージ送信用プレフィックス
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * WebSocketエンドポイントの設定
     * クライアントはこのエンドポイントに接続してWebSocket通信を開始
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:3000", "http://localhost:5173")
                .withSockJS();  // SockJS fallback for browsers without WebSocket support
    }
}
