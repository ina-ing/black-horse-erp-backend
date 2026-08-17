package com.inaing.blackhorse_erp.module.supply.dto.request;

import java.util.List;

import com.inaing.blackhorse_erp.common.dto.pagination.DateRange;
import com.inaing.blackhorse_erp.module.supply.domain.enums.SupplyStatus;

// Resolved, typed filter passed into the service (spec is built from this).
public record SupplyFilter(
        DateRange dateRange,
        List<SupplyStatus> statuses,
        String search) {
}
