package com.example.mos.Repositories;

import com.example.mos.Models.OrderDetail;
import com.example.mos.Models.OrderResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderResponseRepository extends JpaRepository<OrderResponse, Long> {
    Optional<OrderResponse> findByOrderDetail(OrderDetail orderDetail);
    List<OrderResponse> findByOrderDetailIn(List<OrderDetail> orderDetails);
}
