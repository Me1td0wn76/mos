package com.example.mos.Repositories;

import com.example.mos.Models.Order;
import com.example.mos.Models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrder(Order order);
    List<OrderDetail> findByOrderAndStatus(Order order, OrderDetail.DetailStatus status);
    List<OrderDetail> findByStatus(OrderDetail.DetailStatus status);
}
