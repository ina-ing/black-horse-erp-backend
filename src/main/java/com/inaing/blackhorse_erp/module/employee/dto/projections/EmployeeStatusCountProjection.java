package com.inaing.blackhorse_erp.module.employee.dto.projections;

import com.inaing.blackhorse_erp.module.employee.domain.EmployeeStatus;

public interface EmployeeStatusCountProjection {

    EmployeeStatus getStatus();

    long getCount();
}
