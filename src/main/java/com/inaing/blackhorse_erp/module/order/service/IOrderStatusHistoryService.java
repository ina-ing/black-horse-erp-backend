package com.inaing.blackhorse_erp.module.order.service;

import java.util.List;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.OrderStatusHistory;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;

public interface IOrderStatusHistoryService {

    void record(Order order, OrderStatus status, ActionTrigger trigger, AuthPrincipal actor);

    List<OrderStatusHistory> getByOrder(String orderId);
}
