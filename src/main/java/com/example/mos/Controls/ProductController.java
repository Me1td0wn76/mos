package com.example.mos.Controls;

import com.example.mos.Services.ProductService;
import com.example.mos.dto.ApiResponse;
import com.example.mos.dto.ProductDto;
import com.example.mos.dto.ProductRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品コントローラー
 */
@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin
public class ProductController {
    
    private final ProductService productService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProductsByStore(@PathVariable Long storeId) {
        List<ProductDto> products = productService.getProductsByStore(storeId);
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDto> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable Long productId) {
        ProductDto product = productService.getProductById(productId);
        return ResponseEntity.ok(ApiResponse.success(product));
    }
    
    @PatchMapping("/{productId}/sold-out")
    public ResponseEntity<ApiResponse<ProductDto>> updateSoldOutStatus(
            @PathVariable Long productId,
            @RequestParam Boolean isSoldOut) {
        ProductDto product = productService.updateProductSoldOutStatus(productId, isSoldOut);
        return ResponseEntity.ok(ApiResponse.success("Product sold-out status updated", product));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@RequestBody ProductRequest request) {
        log.info("Creating new product: {}", request.getProductName());
        ProductDto product = productService.createProduct(request);
        return ResponseEntity.ok(ApiResponse.success("Product created successfully", product));
    }
    
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductRequest request) {
        log.info("Updating product: {}", productId);
        ProductDto product = productService.updateProduct(productId, request);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", product));
    }
}
