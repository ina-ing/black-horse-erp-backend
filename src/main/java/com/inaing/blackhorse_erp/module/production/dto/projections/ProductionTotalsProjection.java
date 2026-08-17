package com.inaing.blackhorse_erp.module.production.dto.projections;

public interface ProductionTotalsProjection {

    long getTotal();

    long getTotalQuantity();

    long getTotalArticles();
}
