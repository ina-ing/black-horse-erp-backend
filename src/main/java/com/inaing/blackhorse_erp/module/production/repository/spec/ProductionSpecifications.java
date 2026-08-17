package com.inaing.blackhorse_erp.module.production.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.inaing.blackhorse_erp.common.dto.pagination.DateRange;
import com.inaing.blackhorse_erp.module.production.domain.Production;

// Each predicate is a no-op (Specification.unrestricted()) when its input is
// absent, so they compose via Specification.allOf(...) into "any combination, or none".
public final class ProductionSpecifications {

    private ProductionSpecifications() {
    }

    public static Specification<Production> producedBetween(DateRange range) {
        if (range == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.between(
                root.get("productionDate"), range.startInstant(), range.endInstant());
    }

    // The factory can be addressed by either its id or its code.
    public static Specification<Production> factoryIs(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.or(
                cb.equal(root.get("factory").get("id"), identifier),
                cb.equal(root.get("factory").get("code"), identifier));
    }

    public static Specification<Production> matchesSearch(String search) {
        if (search == null || search.isBlank()) {
            return Specification.unrestricted();
        }
        String like = "%" + search.trim().toLowerCase() + "%";

        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("code")), like),
                cb.like(cb.lower(root.get("factory").get("name")), like));
    }
}
