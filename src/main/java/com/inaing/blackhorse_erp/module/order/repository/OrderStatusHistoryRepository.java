package com.inaing.blackhorse_erp.module.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.order.domain.OrderStatusHistory;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, String> {

    List<OrderStatusHistory> findByOrderIdOrderByCreatedAtAsc(String orderId);
}
