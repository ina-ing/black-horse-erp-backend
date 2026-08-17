package com.inaing.blackhorse_erp.module.activityLog.dto.response;

import java.time.Instant;

import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityPriority;

public record ActivityLogResponseDto(
        String id,
        String action,
        String details,
        ActivityPriority priority,
        String actorName,
        String actorRole,
        ActivityEntityType entityType,
        String entityCode,
        Instant occurredAt) {
}
