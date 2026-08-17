package com.inaing.blackhorse_erp.module.order.dto.request;

import java.util.List;

import com.inaing.blackhorse_erp.common.dto.pagination.DateRange;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;

// Resolved, typed filter passed into the service (spec is built from this).
// handledById and retailerId carry the role scoping of the caller.
public record OrderFilter(
        DateRange dateRange,
        List<OrderStatus> statuses,
        String search,
        String handledById,
        String retailerId,
        String retailer) {
}
