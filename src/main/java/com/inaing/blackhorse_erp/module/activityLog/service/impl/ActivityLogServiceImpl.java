package com.inaing.blackhorse_erp.module.activityLog.service.impl;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.activityLog.domain.ActivityLog;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityAction;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.dto.request.ActivityLogFilter;
import com.inaing.blackhorse_erp.module.activityLog.repository.ActivityLogRepository;
import com.inaing.blackhorse_erp.module.activityLog.repository.spec.ActivityLogSpecifications;
import com.inaing.blackhorse_erp.module.activityLog.service.IActivityLogService;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityLogServiceImpl implements IActivityLogService {

    private static final Logger log = LoggerFactory.getLogger(ActivityLogServiceImpl.class);

    private static final String SYSTEM_ACTOR = "System";
    private static final String SYSTEM_ROLE = "AUTOMATED";

    private final ActivityLogRepository activityLogRepository;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public void record(ActivityAction action, String details, ActivityEntityType entityType,
            String entityId, String entityCode, ActionTrigger trigger) {

        write(currentUserProvider.currentPrincipal().orElse(null), action, details, entityType,
                entityId, entityCode, trigger);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(AuthPrincipal actor, ActivityAction action, String details,
            ActivityEntityType entityType, String entityId, String entityCode,
            ActionTrigger trigger) {

        write(actor, action, details, entityType, entityId, entityCode, trigger);
    }

    private void write(AuthPrincipal principal, ActivityAction action, String details,
            ActivityEntityType entityType, String entityId, String entityCode,
            ActionTrigger trigger) {

        try {
            ActivityLog entry = ActivityLog.builder()
                    .action(action)
                    .details(details)
                    .priority(action.getDefaultPriority())
                    .trigger(trigger)
                    .actorId(principal != null ? principal.id() : null)
                    .actorName(principal != null ? principal.name() : SYSTEM_ACTOR)
                    .actorRole(principal != null ? principal.role() : SYSTEM_ROLE)
                    .entityType(entityType)
                    .entityId(entityId)
                    .entityCode(entityCode)
                    .occurredAt(Instant.now())
                    .build();

            activityLogRepository.save(entry);
        } catch (Exception e) {
            log.warn("Could not record activity {}", action, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityLog> getActivityLogs(ActivityLogFilter filter, Pageable pageable) {
        Specification<ActivityLog> spec = Specification.allOf(
                ActivityLogSpecifications.occurredBetween(filter.dateRange()),
                ActivityLogSpecifications.priorityIs(filter.priority()),
                ActivityLogSpecifications.entityTypeIs(filter.entityType()),
                ActivityLogSpecifications.matchesSearch(filter.search()));

        return activityLogRepository.findAll(spec, pageable);
    }
}
