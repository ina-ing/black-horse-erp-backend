package com.inaing.blackhorse_erp.module.retailer.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.inaing.blackhorse_erp.common.domain.Province;
import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;
import com.inaing.blackhorse_erp.module.retailer.domain.enums.BusinessType;

// Each predicate is a no-op (Specification.unrestricted()) when its input is
// absent, so they compose via Specification.allOf(...) into "any combination, or none".
public final class RetailerSpecifications {

    private RetailerSpecifications() {
    }

    public static Specification<Retailer> provinceIs(Province province) {
        if (province == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.equal(root.get("province"), province);
    }

    public static Specification<Retailer> businessTypeIs(BusinessType businessType) {
        if (businessType == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.equal(root.get("businessType"), businessType);
    }

    public static Specification<Retailer> assignedTo(String salesmanId) {
        if (salesmanId == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.equal(root.get("assignedSalesman").get("id"), salesmanId);
    }

    public static Specification<Retailer> matchesSearch(String search) {
        if (search == null || search.isBlank()) {
            return Specification.unrestricted();
        }
        String like = "%" + search.trim().toLowerCase() + "%";

        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("code")), like),
                cb.like(cb.lower(root.get("storeName")), like),
                cb.like(cb.lower(root.get("contactPerson")), like),
                cb.like(cb.lower(root.get("phone")), like));
    }
}
