package com.inaing.blackhorse_erp.module.employee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.role.domain.Role;

public interface EmployeeRepository extends JpaRepository<Employee, String>{
    Optional<Employee> findByPhone(String phone);

    Optional<Employee> findByCode(String code);

    boolean existsByPhone(String phone);

    boolean existsByRole(Role role);

    boolean existsByCode(String code);
}
