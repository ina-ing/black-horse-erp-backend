package com.inaing.blackhorse_erp.module.productionOrder.repository.spec;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.inaing.blackhorse_erp.common.dto.pagination.DateRange;
import com.inaing.blackhorse_erp.module.productionOrder.domain.ProductionOrder;
import com.inaing.blackhorse_erp.module.productionOrder.domain.enums.ProductionOrderStatus;

// Each predicate is a no-op (Specification.unrestricted()) when its input is
// absent, so they compose via Specification.allOf(...) into "any combination, or none".
public final class ProductionOrderSpecifications {

    private ProductionOrderSpecifications() {
    }

    public static Specification<ProductionOrder> orderDateBetween(DateRange range) {
        if (range == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.between(
                root.get("orderDate"), range.startInstant(), range.endInstant());
    }

    public static Specification<ProductionOrder> statusIn(List<ProductionOrderStatus> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> root.get("status").in(statuses);
    }

    public static Specification<ProductionOrder> matchesSearch(String search) {
        if (search == null || search.isBlank()) {
            return Specification.unrestricted();
        }
        String like = "%" + search.trim().toLowerCase() + "%";

        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("code")), like),
                cb.like(cb.lower(root.get("warehouse").get("name")), like));
    }
}
