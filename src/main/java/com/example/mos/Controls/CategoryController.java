package com.example.mos.Controls;

import com.example.mos.Services.CategoryService;
import com.example.mos.dto.ApiResponse;
import com.example.mos.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:5173", "http://localhost:5174"})
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 全カテゴリー取得
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success("カテゴリー一覧を取得しました", categories));
    }

    /**
     * 店舗別カテゴリー取得
     */
    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getCategoriesByStore(@PathVariable Long storeId) {
        List<CategoryDto> categories = categoryService.getCategoriesByStore(storeId);
        return ResponseEntity.ok(ApiResponse.success("カテゴリー一覧を取得しました", categories));
    }
}
