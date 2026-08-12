package com.inaing.blackhorse_erp.module.employee.service;

import java.util.List;

import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.role.domain.Role;

public interface IEmployeeService {

    Employee create(Employee employee);

    Employee getById(String id);

    Employee getByIdentifier(String identifier);

    Employee findByPhone(String phone);

    Employee update(Employee employee);

    String generateEmployeeCode(Role role);

    List<Employee> getByRole(Role role);
}
