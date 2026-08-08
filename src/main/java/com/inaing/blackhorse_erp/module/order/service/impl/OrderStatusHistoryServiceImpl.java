package com.inaing.blackhorse_erp.module.order.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.OrderStatusHistory;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;
import com.inaing.blackhorse_erp.module.order.repository.OrderStatusHistoryRepository;
import com.inaing.blackhorse_erp.module.order.service.IOrderStatusHistoryService;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderStatusHistoryServiceImpl implements IOrderStatusHistoryService {

    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    @Override
    @Transactional
    public void record(Order order, OrderStatus status, ActionTrigger trigger,
            AuthPrincipal principal) {

        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .status(status)
                .trigger(trigger)
                .principalName(principal.name())
                .principalRole(principal.role())
                .build();

        orderStatusHistoryRepository.save(history);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderStatusHistory> getByOrder(String orderId) {
        return orderStatusHistoryRepository.findByOrderIdOrderByCreatedAtAsc(orderId);
    }
}
