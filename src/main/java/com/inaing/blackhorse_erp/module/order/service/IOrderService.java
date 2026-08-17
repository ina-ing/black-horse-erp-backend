package com.inaing.blackhorse_erp.module.order.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;
import com.inaing.blackhorse_erp.module.order.dto.projections.OrderMonthlyVolumeProjection;
import com.inaing.blackhorse_erp.module.order.dto.projections.OrderStatusCountProjection;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderFilter;

public interface IOrderService {

    Order create(Order order);

    Order getByIdentifier(String identifier);

    List<Order> getAll();

    List<Order> getAllByStatus(OrderStatus status);

    Page<Order> getOrders(OrderFilter filter, Pageable pageable);

    List<OrderStatusCountProjection> getStatusCounts(String handledById, String retailerId,
            List<OrderStatus> statuses);

    List<OrderMonthlyVolumeProjection> getMonthlyVolume(Instant from, String handledById,
            String retailerId, List<OrderStatus> statuses);

    Order update(Order order);
}
