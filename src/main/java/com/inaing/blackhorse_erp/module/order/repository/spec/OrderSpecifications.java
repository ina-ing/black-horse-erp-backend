package com.inaing.blackhorse_erp.module.order.repository.spec;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.inaing.blackhorse_erp.common.dto.pagination.DateRange;
import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;

// Each predicate is a no-op (Specification.unrestricted()) when its input is
// absent, so they compose via Specification.allOf(...) into "any combination, or none".
public final class OrderSpecifications {

    private OrderSpecifications() {
    }

    public static Specification<Order> orderDateBetween(DateRange range) {
        if (range == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.between(
                root.get("orderDate"), range.startInstant(), range.endInstant());
    }

    // An empty list means the caller asked for a status its role cannot see, which
    // matches nothing — only null leaves the status unrestricted.
    public static Specification<Order> statusIn(List<OrderStatus> statuses) {
        if (statuses == null) {
            return Specification.unrestricted();
        }
        if (statuses.isEmpty()) {
            return (root, query, cb) -> cb.disjunction();
        }
        return (root, query, cb) -> root.get("status").in(statuses);
    }

    public static Specification<Order> handledBy(String employeeId) {
        if (employeeId == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.equal(root.get("handledBy").get("id"), employeeId);
    }

    public static Specification<Order> placedBy(String retailerId) {
        if (retailerId == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.equal(root.get("retailer").get("id"), retailerId);
    }

    // The retailer can be addressed by either its id or its code.
    public static Specification<Order> retailerIs(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.or(
                cb.equal(root.get("retailer").get("id"), identifier),
                cb.equal(root.get("retailer").get("code"), identifier));
    }

    public static Specification<Order> matchesSearch(String search) {
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
