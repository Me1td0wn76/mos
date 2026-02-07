package com.example.mos.Repositories;

import com.example.mos.Models.Category;
import com.example.mos.Models.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByStore(Store store);
    List<Category> findByStoreAndIsActive(Store store, Boolean isActive);
    List<Category> findByStoreAndIsActiveOrderByDisplayOrderAsc(Store store, Boolean isActive);
}
