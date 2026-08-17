package com.inaing.blackhorse_erp.module.employee.dto.request;

import java.util.List;

import com.inaing.blackhorse_erp.module.employee.domain.EmployeeStatus;
import com.inaing.blackhorse_erp.module.role.domain.Role;

// Resolved, typed filter passed into the service (spec is built from this).
public record EmployeeFilter(
        List<Role> roles,
        EmployeeStatus status,
        String search) {
}
