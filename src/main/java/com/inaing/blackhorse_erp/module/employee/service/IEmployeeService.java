package com.inaing.blackhorse_erp.module.employee.service;

import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.role.domain.Role;

public interface IEmployeeService {

    Employee create(Employee employee);

    Employee getById(String id);

    Employee getByIdentifier(String identifier);

    Employee findByPhone(String phone);

    String generateEmployeeCode(Role role);
}
