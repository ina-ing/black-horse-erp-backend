package com.inaing.blackhorse_erp.module.returns.dto.request;

import java.util.List;

import com.inaing.blackhorse_erp.common.dto.pagination.DateRange;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnReason;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;

// Resolved, typed filter passed into the service (spec is built from this).
// handledById, retailerId and reasons carry the role scoping of the caller.
public record ReturnFilter(
        DateRange dateRange,
        List<ReturnStatus> statuses,
        List<ReturnReason> reasons,
        String search,
        String handledById,
        String retailerId) {
}
