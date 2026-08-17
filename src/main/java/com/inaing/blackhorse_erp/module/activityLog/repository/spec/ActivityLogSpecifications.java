package com.inaing.blackhorse_erp.module.activityLog.repository.spec;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.inaing.blackhorse_erp.common.dto.pagination.DateRange;
import com.inaing.blackhorse_erp.module.activityLog.domain.ActivityLog;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityAction;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityPriority;

// Each predicate is a no-op (Specification.unrestricted()) when its input is
// absent, so they compose via Specification.allOf(...) into "any combination, or none".
public final class ActivityLogSpecifications {

    private ActivityLogSpecifications() {
    }

    public static Specification<ActivityLog> occurredBetween(DateRange range) {
        if (range == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.between(
                root.get("occurredAt"), range.startInstant(), range.endInstant());
    }

    public static Specification<ActivityLog> priorityIs(ActivityPriority priority) {
        if (priority == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.equal(root.get("priority"), priority);
    }

    public static Specification<ActivityLog> entityTypeIs(ActivityEntityType entityType) {
        if (entityType == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.equal(root.get("entityType"), entityType);
    }

    // The action column holds the enum name, so a text search matches it through the
    // constants whose name or label contains the term, and falls back to the free text
    // columns for everything else.
    public static Specification<ActivityLog> matchesSearch(String search) {
        if (search == null || search.isBlank()) {
            return Specification.unrestricted();
        }
        String term = search.trim().toLowerCase();
        String like = "%" + term + "%";
        List<ActivityAction> actions = Arrays.stream(ActivityAction.values())
                .filter(action -> action.name().toLowerCase().contains(term)
                        || action.getLabel().toLowerCase().contains(term))
                .toList();

        return (root, query, cb) -> {
            var byText = cb.or(
                    cb.like(cb.lower(root.get("actorName")), like),
                    cb.like(cb.lower(root.get("details")), like),
                    cb.like(cb.lower(root.get("entityCode")), like));

            return actions.isEmpty()
                    ? byText
                    : cb.or(byText, root.get("action").in(actions));
        };
    }
}
