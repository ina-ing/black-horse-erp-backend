package com.inaing.blackhorse_erp.module.order.service;

import java.util.List;

import com.inaing.blackhorse_erp.module.order.domain.Order;

public interface IOrderService {

    Order create(Order order);

    Order getByIdentifier(String identifier);

    List<Order> getAll();

    Order update(Order order);

    String generateOrderCode();
}
