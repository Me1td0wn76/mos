package com.example.mos.Repositories;

import com.example.mos.Models.Order;
import com.example.mos.Models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder(Order order);
    List<Payment> findByStatus(Payment.PaymentStatus status);
    Optional<Payment> findByPosTransactionId(String posTransactionId);
}
