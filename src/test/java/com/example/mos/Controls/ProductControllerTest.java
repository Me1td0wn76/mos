package com.example.mos.Controls;

import com.example.mos.Services.ProductService;
import com.example.mos.dto.ProductDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ProductController の統合テスト
 */
@WebMvcTest(ProductController.class)
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("店舗別商品一覧取得")
    void testGetProductsByStore() throws Exception {
        // Arrange
        List<ProductDto> products = new ArrayList<>();
        ProductDto product = new ProductDto();
        product.setProductId(1L);
        product.setProductName("ねぎま");
        product.setPrice(BigDecimal.valueOf(180));
        product.setIsAvailable(true);
        product.setIsSoldOut(false);
        product.setCategoryNames(new HashSet<>());
        products.add(product);

        when(productService.getProductsByStore(1L))
                .thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/products/store/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].productName").value("ねぎま"))
                .andExpect(jsonPath("$.data[0].price").value(180));
    }

    @Test
    @DisplayName("商品詳細取得")
    void testGetProductById() throws Exception {
        // Arrange
        ProductDto product = new ProductDto();
        product.setProductId(1L);
        product.setProductName("ねぎま");
        product.setDescription("定番の焼き鳥");
        product.setPrice(BigDecimal.valueOf(180));
        product.setIsAvailable(true);
        product.setIsSoldOut(false);
        product.setCategoryNames(new HashSet<>());

        when(productService.getProductById(1L))
                .thenReturn(product);

        // Act & Assert
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.productId").value(1))
                .andExpect(jsonPath("$.data.productName").value("ねぎま"))
                .andExpect(jsonPath("$.data.description").value("定番の焼き鳥"));
    }

    @Test
    @DisplayName("売り切れステータス更新")
    void testUpdateSoldOutStatus() throws Exception {
        // Arrange
        ProductDto product = new ProductDto();
        product.setProductId(1L);
        product.setProductName("ねぎま");
        product.setPrice(BigDecimal.valueOf(180));
        product.setIsSoldOut(true);
        product.setCategoryNames(new HashSet<>());

        when(productService.updateProductSoldOutStatus(1L, true))
                .thenReturn(product);

        // Act & Assert
        mockMvc.perform(patch("/api/products/1/sold-out")
                        .param("isSoldOut", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.isSoldOut").value(true));
    }
}
