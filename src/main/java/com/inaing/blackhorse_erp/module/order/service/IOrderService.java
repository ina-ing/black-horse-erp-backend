package com.inaing.blackhorse_erp.module.order.service;

import java.util.List;

import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;

public interface IOrderService {

    Order create(Order order);

    Order getByIdentifier(String identifier);

    List<Order> getAll();

    List<Order> getAllByStatus(OrderStatus status);

    Order update(Order order);
}
