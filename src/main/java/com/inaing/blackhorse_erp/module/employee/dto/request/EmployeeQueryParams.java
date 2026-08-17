package com.inaing.blackhorse_erp.module.employee.dto.request;

import java.util.List;

import com.inaing.blackhorse_erp.common.dto.request.PageableRequest;
import com.inaing.blackhorse_erp.module.employee.domain.EmployeeStatus;
import com.inaing.blackhorse_erp.module.role.domain.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeQueryParams extends PageableRequest {

    private List<Role> roles;

    private EmployeeStatus status;
}
