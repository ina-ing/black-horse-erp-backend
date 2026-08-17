package com.inaing.blackhorse_erp.module.activityLog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.activityLog.domain.ActivityLog;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityAction;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.dto.request.ActivityLogFilter;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;

public interface IActivityLogService {

    void record(ActivityAction action, String details, ActivityEntityType entityType,
            String entityId, String entityCode, ActionTrigger trigger);

    void record(AuthPrincipal actor, ActivityAction action, String details,
            ActivityEntityType entityType, String entityId, String entityCode,
            ActionTrigger trigger);

    Page<ActivityLog> getActivityLogs(ActivityLogFilter filter, Pageable pageable);
}
