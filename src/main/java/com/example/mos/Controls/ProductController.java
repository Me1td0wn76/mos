package com.example.mos.Controls;

import com.example.mos.Services.ProductService;
import com.example.mos.dto.ApiResponse;
import com.example.mos.dto.ProductDto;
import com.example.mos.dto.ProductRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

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
    
    @PostMapping("/upload-image")
    public ResponseEntity<ApiResponse<String>> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("ファイルが選択されていません"));
            }
            
            // ファイルタイプのチェック
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("画像ファイルのみアップロード可能です"));
            }
            
            // ファイルサイズのチェック (5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("ファイルサイズは5MB以下にしてください"));
            }
            
            // アップロードディレクトリの作成
            Path uploadDir = Paths.get("uploads/products");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            
            // ユニークなファイル名を生成
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + extension;
            
            // ファイルを保存
            Path filePath = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // URLを返す
            String imageUrl = "/uploads/products/" + filename;
            log.info("Image uploaded successfully: {}", imageUrl);
            
            return ResponseEntity.ok(ApiResponse.success("画像をアップロードしました", imageUrl));
            
        } catch (IOException e) {
            log.error("Failed to upload image", e);
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error("画像のアップロードに失敗しました: " + e.getMessage()));
        }
    }
}
