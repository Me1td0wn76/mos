package com.example.mos.Services;

import com.example.mos.Models.*;
import com.example.mos.Repositories.*;
import com.example.mos.dto.NotificationMessage;
import com.example.mos.dto.OrderCreateRequest;
import com.example.mos.dto.OrderItemRequest;
import com.example.mos.dto.OrderResponse;
import com.example.mos.exception.InvalidRequestException;
import com.example.mos.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 注文サービス
 */
@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailOptionRepository orderDetailOptionRepository;
    private final TableInfoRepository tableInfoRepository;
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final SimpMessagingTemplate messagingTemplate;
    
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        // QRコードからテーブル情報を取得
        TableInfo table = tableInfoRepository.findByQrCode(request.getQrCode())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found for QR code"));
        
        // 注文作成
        Order order = new Order();
        order.setTable(table);
        order.setPlanType(Order.PlanType.valueOf(request.getPlanType()));
        order.setPlanDuration(request.getPlanDuration());
        order.setStatus(Order.OrderStatus.CONFIRMED);
        
        // 飲み放題の場合、タイマー開始
        if (!request.getPlanType().equals("NORMAL")) {
            order.setTimerStartTime(LocalDateTime.now());
        }
        
        order = orderRepository.save(order);
        
        // 注文詳細作成
        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            
            if (!product.getIsAvailable() || product.getIsSoldOut()) {
                throw new InvalidRequestException("Product is not available: " + product.getProductName());
            }
            
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(itemRequest.getQuantity());
            orderDetail.setUnitPrice(product.getPrice());
            orderDetail.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
            orderDetail.setRemarks(itemRequest.getRemarks());
            orderDetail.setStatus(OrderDetail.DetailStatus.PENDING);
            
            orderDetail = orderDetailRepository.save(orderDetail);
            
            // オプション処理
            if (itemRequest.getOptionIds() != null && !itemRequest.getOptionIds().isEmpty()) {
                for (Long optionId : itemRequest.getOptionIds()) {
                    ProductOption productOption = productOptionRepository.findById(optionId)
                            .orElseThrow(() -> new ResourceNotFoundException("Product option not found"));
                    
                    OrderDetailOption detailOption = new OrderDetailOption();
                    detailOption.setOrderDetail(orderDetail);
                    detailOption.setProductOption(productOption);
                    detailOption.setAdditionalPrice(productOption.getAdditionalPrice());
                    
                    orderDetailOptionRepository.save(detailOption);
                }
            }
        }
        
        // テーブルステータスを使用中に更新
        if (table.getStatus() == TableInfo.TableStatus.AVAILABLE) {
            table.setStatus(TableInfo.TableStatus.IN_USE);
            tableInfoRepository.save(table);
        }
        
        // WebSocket通知: 新規注文
        OrderResponse orderResponse = convertToOrderResponse(order);
        NotificationMessage notification = NotificationMessage.create(
                NotificationMessage.NotificationType.NEW_ORDER,
                table.getStore().getStoreId(),
                order.getOrderId(),
                "新しい注文が入りました（テーブル" + table.getTableNumber() + "）",
                orderResponse
        );
        messagingTemplate.convertAndSend("/topic/store/" + table.getStore().getStoreId() + "/orders", notification);
        
        return orderResponse;
    }
    
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStore(Long storeId) {
        List<Order> orders = orderRepository.findByStoreIdAndStatus(storeId, Order.OrderStatus.CONFIRMED);
        return orders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return convertToOrderResponse(order);
    }
    
    @Transactional
    public void updateOrderDetailStatus(Long orderDetailId, String status) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Order detail not found"));
        
        orderDetail.setStatus(OrderDetail.DetailStatus.valueOf(status));
        orderDetailRepository.save(orderDetail);
        
        // WebSocket通知: 注文ステータス更新
        Order order = orderDetail.getOrder();
        Long storeId = order.getTable().getStore().getStoreId();
        NotificationMessage notification = NotificationMessage.create(
                NotificationMessage.NotificationType.ORDER_STATUS_UPDATE,
                storeId,
                order.getOrderId(),
                "注文ステータスが更新されました: " + orderDetail.getProduct().getProductName() + " → " + status,
                convertToOrderResponse(order)
        );
        messagingTemplate.convertAndSend("/topic/store/" + storeId + "/orders", notification);
    }
    
    private OrderResponse convertToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setTableNumber(order.getTable().getTableNumber());
        response.setPlanType(order.getPlanType().name());
        response.setPlanDuration(order.getPlanDuration());
        response.setTimerStartTime(order.getTimerStartTime());
        response.setStatus(order.getStatus().name());
        response.setCreatedAt(order.getCreatedAt());
        
        List<OrderDetail> details = orderDetailRepository.findByOrder(order);
        response.setDetails(details.stream()
                .map(this::convertToOrderDetailDto)
                .collect(Collectors.toList()));
        
        return response;
    }
    
    private OrderResponse.OrderDetailDto convertToOrderDetailDto(OrderDetail detail) {
        OrderResponse.OrderDetailDto dto = new OrderResponse.OrderDetailDto();
        dto.setOrderDetailId(detail.getOrderDetailId());
        dto.setProductName(detail.getProduct().getProductName());
        dto.setQuantity(detail.getQuantity());
        dto.setUnitPrice(detail.getUnitPrice());
        dto.setSubtotal(detail.getSubtotal());
        dto.setRemarks(detail.getRemarks());
        dto.setStatus(detail.getStatus().name());
        
        List<OrderDetailOption> options = orderDetailOptionRepository.findByOrderDetail(detail);
        dto.setOptions(options.stream()
                .map(opt -> opt.getProductOption().getOptionName() + ": " + opt.getProductOption().getOptionValue())
                .collect(Collectors.toList()));
        
        return dto;
    }
}
