package com.example.mos.Repositories;

import com.example.mos.Models.Order;
import com.example.mos.Models.TableInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByTable(TableInfo table);
    List<Order> findByTableAndStatus(TableInfo table, Order.OrderStatus status);
    List<Order> findByStatus(Order.OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.table.store.storeId = :storeId AND o.status = :status")
    List<Order> findByStoreIdAndStatus(@Param("storeId") Long storeId, @Param("status") Order.OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.table.store.storeId = :storeId AND o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findByStoreIdAndCreatedAtBetween(@Param("storeId") Long storeId, 
                                                  @Param("startDate") LocalDateTime startDate, 
                                                  @Param("endDate") LocalDateTime endDate);
}
