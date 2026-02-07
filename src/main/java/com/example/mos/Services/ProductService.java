package com.example.mos.Services;

import com.example.mos.Models.Category;
import com.example.mos.Models.Product;
import com.example.mos.Models.Store;
import com.example.mos.Repositories.ProductRepository;
import com.example.mos.Repositories.StoreRepository;
import com.example.mos.dto.ProductDto;
import com.example.mos.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品サービス
 */
@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    
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
