package com.inaing.blackhorse_erp.module.employee.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.dto.projections.EmployeeStatusCountProjection;
import com.inaing.blackhorse_erp.module.employee.dto.request.EmployeeFilter;
import com.inaing.blackhorse_erp.module.role.domain.Role;

public interface IEmployeeService {

    Employee create(Employee employee);

    Employee getById(String id);

    Employee getByIdentifier(String identifier);

    Employee findByPhone(String phone);

    Employee update(Employee employee);

    String generateEmployeeCode(Role role);

    List<Employee> getAll();

    Page<Employee> getEmployees(EmployeeFilter filter, Pageable pageable);

    List<EmployeeStatusCountProjection> getStatusCounts(List<Role> roles);

    List<Employee> getByRole(Role role);

    boolean existsByRole(Role role);
}
