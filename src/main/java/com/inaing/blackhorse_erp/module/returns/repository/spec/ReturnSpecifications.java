package com.inaing.blackhorse_erp.module.returns.repository.spec;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.inaing.blackhorse_erp.common.dto.pagination.DateRange;
import com.inaing.blackhorse_erp.module.returns.domain.Return;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnReason;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;

// Each predicate is a no-op (Specification.unrestricted()) when its input is
// absent, so they compose via Specification.allOf(...) into "any combination, or none".
public final class ReturnSpecifications {

    private ReturnSpecifications() {
    }

    public static Specification<Return> returnDateBetween(DateRange range) {
        if (range == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.between(
                root.get("returnDate"), range.startInstant(), range.endInstant());
    }

    public static Specification<Return> statusIn(List<ReturnStatus> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> root.get("status").in(statuses);
    }

    // An empty list means the caller asked for a reason its role cannot see, which
    // matches nothing — only null leaves the reason unrestricted.
    public static Specification<Return> reasonIn(List<ReturnReason> reasons) {
        if (reasons == null) {
            return Specification.unrestricted();
        }
        if (reasons.isEmpty()) {
            return (root, query, cb) -> cb.disjunction();
        }
        return (root, query, cb) -> root.get("reason").in(reasons);
    }

    public static Specification<Return> handledBy(String employeeId) {
        if (employeeId == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.equal(root.get("handledBy").get("id"), employeeId);
    }

    public static Specification<Return> placedBy(String retailerId) {
        if (retailerId == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.equal(root.get("retailer").get("id"), retailerId);
    }

    public static Specification<Return> matchesSearch(String search) {
        if (search == null || search.isBlank()) {
            return Specification.unrestricted();
        }
        String like = "%" + search.trim().toLowerCase() + "%";

        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("code")), like),
                cb.like(cb.lower(root.get("retailer").get("storeName")), like),
                cb.like(cb.lower(root.get("handledBy").get("fullname")), like));
    }
}
