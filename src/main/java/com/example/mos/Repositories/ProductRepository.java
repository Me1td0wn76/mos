package com.example.mos.Repositories;

import com.example.mos.Models.Product;
import com.example.mos.Models.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStore(Store store);
    List<Product> findByStoreAndIsAvailable(Store store, Boolean isAvailable);
    List<Product> findByStoreAndIsSoldOut(Store store, Boolean isSoldOut);
    
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.categoryId = :categoryId AND p.isAvailable = true")
    List<Product> findByCategoryIdAndIsAvailable(@Param("categoryId") Long categoryId);
    
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE p.store = :store AND c.categoryId = :categoryId")
    List<Product> findByStoreAndCategoryId(@Param("store") Store store, @Param("categoryId") Long categoryId);
}
