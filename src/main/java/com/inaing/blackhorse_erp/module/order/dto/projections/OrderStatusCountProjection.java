package com.inaing.blackhorse_erp.module.order.dto.projections;

import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;

public interface OrderStatusCountProjection {

    OrderStatus getStatus();

    long getCount();
}
