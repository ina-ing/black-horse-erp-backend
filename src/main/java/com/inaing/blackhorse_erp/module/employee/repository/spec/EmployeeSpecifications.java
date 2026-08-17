package com.inaing.blackhorse_erp.module.employee.repository.spec;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.domain.EmployeeStatus;
import com.inaing.blackhorse_erp.module.role.domain.Role;

// Each predicate is a no-op (Specification.unrestricted()) when its input is
// absent, so they compose via Specification.allOf(...) into "any combination, or none".
public final class EmployeeSpecifications {

    private EmployeeSpecifications() {
    }

    public static Specification<Employee> roleIn(List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> root.get("role").in(roles);
    }

    public static Specification<Employee> statusIs(EmployeeStatus status) {
        if (status == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Employee> matchesSearch(String search) {
        if (search == null || search.isBlank()) {
            return Specification.unrestricted();
        }
        String like = "%" + search.trim().toLowerCase() + "%";

        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("fullname")), like),
                cb.like(cb.lower(root.get("code")), like),
                cb.like(cb.lower(root.get("phone")), like),
                cb.like(cb.lower(root.get("email")), like));
    }
}
