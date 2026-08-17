package com.inaing.blackhorse_erp.module.activityLog.dto.request;

import com.inaing.blackhorse_erp.common.dto.pagination.DateRange;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityPriority;

public record ActivityLogFilter(
        DateRange dateRange,
        ActivityPriority priority,
        ActivityEntityType entityType,
        String search) {
}
