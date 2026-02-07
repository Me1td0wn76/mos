package com.example.mos.Services;

import com.example.mos.Models.Product;
import com.example.mos.Models.Store;
import com.example.mos.Repositories.ProductRepository;
import com.example.mos.Repositories.StoreRepository;
import com.example.mos.dto.ProductDto;
import com.example.mos.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * ProductService のユニットテスト
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private ProductService productService;

    private Store testStore;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testStore = new Store();
        testStore.setStoreId(1L);
        testStore.setStoreName("テスト店舗");

        testProduct = new Product();
        testProduct.setProductId(1L);
        testProduct.setProductName("ねぎま");
        testProduct.setDescription("定番の焼き鳥");
        testProduct.setPrice(BigDecimal.valueOf(180));
        testProduct.setImageUrl("/images/negima.jpg");
        testProduct.setIsAvailable(true);
        testProduct.setIsSoldOut(false);
        testProduct.setStore(testStore);
        testProduct.setCategories(new HashSet<>());
    }

    @Test
    @DisplayName("店舗別商品一覧取得")
    void testGetProductsByStore() {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(testProduct);

        when(storeRepository.findById(1L))
                .thenReturn(Optional.of(testStore));
        when(productRepository.findByStoreAndIsAvailable(testStore, true))
                .thenReturn(products);

        // Act
        List<ProductDto> result = productService.getProductsByStore(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ねぎま", result.get(0).getProductName());
        assertEquals(BigDecimal.valueOf(180), result.get(0).getPrice());
    }

    @Test
    @DisplayName("存在しない店舗ID")
    void testGetProductsByStore_StoreNotFound() {
        // Arrange
        when(storeRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductsByStore(999L);
        });
    }

    @Test
    @DisplayName("商品詳細取得")
    void testGetProductById() {
        // Arrange
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(testProduct));

        // Act
        ProductDto result = productService.getProductById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getProductId());
        assertEquals("ねぎま", result.getProductName());
        assertEquals("定番の焼き鳥", result.getDescription());
    }

    @Test
    @DisplayName("売り切れステータス更新")
    void testUpdateProductSoldOutStatus() {
        // Arrange
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class)))
                .thenReturn(testProduct);

        // Act
        ProductDto result = productService.updateProductSoldOutStatus(1L, true);

        // Assert
        assertNotNull(result);
        assertTrue(result.getIsSoldOut());
        verify(productRepository, times(1)).save(argThat(product ->
                product.getIsSoldOut() == true
        ));
    }
}
