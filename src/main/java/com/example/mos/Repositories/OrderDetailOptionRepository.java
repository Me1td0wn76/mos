package com.example.mos.Repositories;

import com.example.mos.Models.OrderDetail;
import com.example.mos.Models.OrderDetailOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailOptionRepository extends JpaRepository<OrderDetailOption, Long> {
    List<OrderDetailOption> findByOrderDetail(OrderDetail orderDetail);
}
