package com.example.mos.Services;

import com.example.mos.Models.Category;
import com.example.mos.Models.Store;
import com.example.mos.Repositories.CategoryRepository;
import com.example.mos.Repositories.StoreRepository;
import com.example.mos.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    /**
     * 店舗の全カテゴリーを取得
     */
    public List<CategoryDto> getCategoriesByStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        
        List<Category> categories = categoryRepository.findByStoreAndIsActiveOrderByDisplayOrderAsc(store, true);
        
        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 全カテゴリーを取得（店舗ID=1固定）
     */
    public List<CategoryDto> getAllCategories() {
        return getCategoriesByStore(1L);
    }

    private CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryName(category.getCategoryName());
        dto.setDisplayOrder(category.getDisplayOrder());
        dto.setIsActive(category.getIsActive());
        return dto;
    }
}
