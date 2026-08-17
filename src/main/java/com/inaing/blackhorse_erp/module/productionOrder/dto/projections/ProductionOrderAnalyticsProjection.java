package com.inaing.blackhorse_erp.module.productionOrder.dto.projections;

import com.inaing.blackhorse_erp.module.productionOrder.domain.enums.ProductionOrderStatus;

public interface ProductionOrderAnalyticsProjection {

    ProductionOrderStatus getStatus();

    long getCount();

    long getQuantity();
}
