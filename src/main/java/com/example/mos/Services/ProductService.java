package com.example.mos.Services;

import com.example.mos.Models.Category;
import com.example.mos.Models.Product;
import com.example.mos.Models.Store;
import com.example.mos.Repositories.CategoryRepository;
import com.example.mos.Repositories.ProductRepository;
import com.example.mos.Repositories.StoreRepository;
import com.example.mos.dto.ProductDto;
import com.example.mos.dto.ProductRequest;
import com.example.mos.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品サービス
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
        
        return productRepository.findByStoreAndIsAvailable(store, true).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndIsAvailable(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return convertToDto(product);
    }
    
    @Transactional
    public ProductDto updateProductSoldOutStatus(Long productId, Boolean isSoldOut) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setIsSoldOut(isSoldOut);
        productRepository.save(product);
        return convertToDto(product);
    }
    
    @Transactional
    public ProductDto createProduct(ProductRequest request) {
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
        
        Product product = new Product();
        product.setStore(store);
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setIsAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true);
        product.setIsSoldOut(request.getIsSoldOut() != null ? request.getIsSoldOut() : false);
        
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            Set<Category> categories = new HashSet<>();
            for (Long categoryId : request.getCategoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));
                categories.add(category);
            }
            product.setCategories(categories);
        }
        
        product = productRepository.save(product);
        log.info("Created new product: {}", product.getProductName());
        return convertToDto(product);
    }
    
    @Transactional
    public ProductDto updateProduct(Long productId, ProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        
        if (request.getStoreId() != null) {
            Store store = storeRepository.findById(request.getStoreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
            product.setStore(store);
        }
        
        if (request.getProductName() != null) {
            product.setProductName(request.getProductName());
        }
        
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        
        if (request.getImageUrl() != null) {
            product.setImageUrl(request.getImageUrl());
        }
        
        if (request.getIsAvailable() != null) {
            product.setIsAvailable(request.getIsAvailable());
        }
        
        if (request.getIsSoldOut() != null) {
            product.setIsSoldOut(request.getIsSoldOut());
        }
        
        if (request.getCategoryIds() != null) {
            Set<Category> categories = new HashSet<>();
            for (Long categoryId : request.getCategoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));
                categories.add(category);
            }
            product.setCategories(categories);
        }
        
        product = productRepository.save(product);
        log.info("Updated product: {}", product.getProductName());
        return convertToDto(product);
    }
    
    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());
        dto.setIsAvailable(product.getIsAvailable());
        dto.setIsSoldOut(product.getIsSoldOut());
        dto.setCategoryNames(product.getCategories().stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toSet()));
        return dto;
    }
}
