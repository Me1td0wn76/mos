package com.example.mos.Controls;

import com.example.mos.Services.OrderService;
import com.example.mos.dto.ApiResponse;
import com.example.mos.dto.OrderCreateRequest;
import com.example.mos.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 注文コントローラー
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@RequestBody OrderCreateRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.ok(ApiResponse.success("Order created successfully", response));
    }
    
    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByStore(@PathVariable Long storeId) {
        List<OrderResponse> orders = orderService.getOrdersByStore(storeId);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long orderId) {
        OrderResponse order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }
    
    @PatchMapping("/details/{orderDetailId}/status")
    public ResponseEntity<ApiResponse<Void>> updateOrderDetailStatus(
            @PathVariable Long orderDetailId,
            @RequestParam String status) {
        orderService.updateOrderDetailStatus(orderDetailId, status);
        return ResponseEntity.ok(ApiResponse.success("Order detail status updated", null));
    }
}
