package com.inaing.blackhorse_erp.module.productionOrder.dto.request;

import java.util.List;

import com.inaing.blackhorse_erp.common.dto.pagination.DateRange;
import com.inaing.blackhorse_erp.module.productionOrder.domain.enums.ProductionOrderStatus;

// Resolved, typed filter passed into the service (spec is built from this).
public record ProductionOrderFilter(
        DateRange dateRange,
        List<ProductionOrderStatus> statuses,
        String search) {
}
