package com.example.mos.Repositories;

import com.example.mos.Models.Product;
import com.example.mos.Models.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    List<ProductOption> findByProduct(Product product);
    List<ProductOption> findByProductAndIsAvailable(Product product, Boolean isAvailable);
}
