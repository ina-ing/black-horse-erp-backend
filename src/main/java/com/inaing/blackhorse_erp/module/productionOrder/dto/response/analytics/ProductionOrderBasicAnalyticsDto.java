package com.inaing.blackhorse_erp.module.productionOrder.dto.response.analytics;

import java.util.Map;

import com.inaing.blackhorse_erp.module.productionOrder.domain.enums.ProductionOrderStatus;

public record ProductionOrderBasicAnalyticsDto(
        long total,
        long totalQuantity,
        Map<ProductionOrderStatus, Long> byStatus) {
}
