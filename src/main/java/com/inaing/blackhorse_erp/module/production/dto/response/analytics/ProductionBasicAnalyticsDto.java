package com.inaing.blackhorse_erp.module.production.dto.response.analytics;

import com.inaing.blackhorse_erp.module.production.dto.projections.ProductionTotalsProjection;

public record ProductionBasicAnalyticsDto(
        long total,
        long thisMonth,
        long totalQuantity,
        long totalArticles) {

    public static ProductionBasicAnalyticsDto of(ProductionTotalsProjection totals, long thisMonth) {
        return new ProductionBasicAnalyticsDto(
                totals.getTotal(), thisMonth, totals.getTotalQuantity(), totals.getTotalArticles());
    }
}
