package com.example.mos.Services;

import com.example.mos.Models.*;
import com.example.mos.Repositories.*;
import com.example.mos.dto.OrderCreateRequest;
import com.example.mos.dto.OrderItemRequest;
import com.example.mos.dto.OrderResponse;
import com.example.mos.exception.InvalidRequestException;
import com.example.mos.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * OrderService のユニットテスト
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private OrderDetailOptionRepository orderDetailOptionRepository;

    @Mock
    private TableInfoRepository tableInfoRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductOptionRepository productOptionRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private OrderService orderService;

    private TableInfo testTable;
    private Product testProduct;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        Store testStore = new Store();
        testStore.setStoreId(1L);
        testStore.setStoreName("テスト店舗");

        testTable = new TableInfo();
        testTable.setTableId(1L);
        testTable.setTableNumber("A01");
        testTable.setStore(testStore);
        testTable.setQrCode("MOS:STORE:1:TABLE:1");
        testTable.setStatus(TableInfo.TableStatus.AVAILABLE);

        testProduct = new Product();
        testProduct.setProductId(1L);
        testProduct.setProductName("ねぎま");
        testProduct.setPrice(BigDecimal.valueOf(180));
        testProduct.setIsAvailable(true);
        testProduct.setIsSoldOut(false);
        testProduct.setStore(testStore);

        testOrder = new Order();
        testOrder.setOrderId(1L);
        testOrder.setTable(testTable);
        testOrder.setPlanType(Order.PlanType.NORMAL);
        testOrder.setStatus(Order.OrderStatus.CONFIRMED);
    }

    @Test
    @DisplayName("注文作成 - 通常プラン")
    void testCreateOrder_NormalPlan() {
        // Arrange
        OrderCreateRequest request = new OrderCreateRequest();
        request.setQrCode("MOS:STORE:1:TABLE:1");
        request.setPlanType("NORMAL");

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(2);
        itemRequest.setRemarks("塩で");

        List<OrderItemRequest> items = new ArrayList<>();
        items.add(itemRequest);
        request.setItems(items);
        
        testOrder.setTable(testTable);

        when(storeRepository.findById(1L))
                .thenReturn(Optional.of(testTable.getStore()));
        when(tableInfoRepository.findByStoreAndTableNumber(any(Store.class), eq("MOS:STORE:1:TABLE:1")))
                .thenReturn(Optional.of(testTable));
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(testProduct));
        when(orderRepository.save(any(Order.class)))
                .thenReturn(testOrder);
        when(orderDetailRepository.save(any(OrderDetail.class)))
                .thenReturn(new OrderDetail());
        when(orderDetailRepository.findByOrder(any()))
                .thenReturn(new ArrayList<>());

        // Act
        OrderResponse response = orderService.createOrder(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getOrderId());
        assertEquals("A01", response.getTableNumber());
        assertEquals("NORMAL", response.getPlanType());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderDetailRepository, times(1)).save(any(OrderDetail.class));
        verify(messagingTemplate, times(1)).convertAndSend(
                eq("/topic/store/1/orders"),
                any(Object.class)
        );
    }

    @Test
    @DisplayName("注文作成 - QRコードが無効")
    void testCreateOrder_InvalidQRCode() {
        // Arrange
        OrderCreateRequest request = new OrderCreateRequest();
        request.setQrCode("INVALID_QR");
        request.setPlanType("NORMAL");
        request.setItems(new ArrayList<>());

        when(storeRepository.findById(1L))
                .thenReturn(Optional.of(testTable.getStore()));
        when(tableInfoRepository.findByStoreAndTableNumber(any(Store.class), eq("INVALID_QR")))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.createOrder(request);
        });
    }

    @Test
    @DisplayName("注文作成 - 商品が売り切れ")
    void testCreateOrder_ProductSoldOut() {
        // Arrange
        testProduct.setIsSoldOut(true);

        OrderCreateRequest request = new OrderCreateRequest();
        request.setQrCode("MOS:STORE:1:TABLE:1");
        request.setPlanType("NORMAL");

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(1);

        List<OrderItemRequest> items = new ArrayList<>();
        items.add(itemRequest);
        request.setItems(items);

        when(storeRepository.findById(1L))
                .thenReturn(Optional.of(testTable.getStore()));
        when(tableInfoRepository.findByStoreAndTableNumber(any(Store.class), eq("MOS:STORE:1:TABLE:1")))
                .thenReturn(Optional.of(testTable));
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(testProduct));
        when(orderRepository.save(any(Order.class)))
                .thenReturn(testOrder);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            orderService.createOrder(request);
        });
    }

    @Test
    @DisplayName("注文詳細のステータス更新")
    void testUpdateOrderDetailStatus() {
        // Arrange
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderDetailId(1L);
        orderDetail.setStatus(OrderDetail.DetailStatus.PENDING);
        orderDetail.setOrder(testOrder);
        orderDetail.setProduct(testProduct);
        
        testOrder.setTable(testTable);

        when(orderDetailRepository.findById(1L))
                .thenReturn(Optional.of(orderDetail));
        when(orderDetailRepository.save(any(OrderDetail.class)))
                .thenReturn(orderDetail);
        when(orderDetailRepository.findByOrder(testOrder))
                .thenReturn(new ArrayList<>());

        // Act
        orderService.updateOrderDetailStatus(1L, "SERVED");

        // Assert
        verify(orderDetailRepository, times(1)).save(argThat(detail ->
                detail.getStatus() == OrderDetail.DetailStatus.SERVED
        ));
        verify(messagingTemplate, times(1)).convertAndSend(
                eq("/topic/store/1/orders"),
                any(Object.class)
        );
    }
}
