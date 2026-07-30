package com.inaing.blackhorse_erp.module.order.service;

import java.util.List;

import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.OrderStatusHistory;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderHistoryTrigger;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;

public interface IOrderStatusHistoryService {

    void record(Order order, OrderStatus toStatus, OrderHistoryTrigger trigger, AuthPrincipal actor);

    List<OrderStatusHistory> getByOrder(String orderId);
}
