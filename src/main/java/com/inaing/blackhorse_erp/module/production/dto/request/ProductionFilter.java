package com.inaing.blackhorse_erp.module.production.dto.request;

import com.inaing.blackhorse_erp.common.dto.pagination.DateRange;

// Resolved, typed filter passed into the service (spec is built from this).
public record ProductionFilter(
        DateRange dateRange,
        String factory,
        String search) {
}
