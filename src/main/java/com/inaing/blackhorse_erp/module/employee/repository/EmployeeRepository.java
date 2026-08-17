package com.inaing.blackhorse_erp.module.employee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.dto.projections.EmployeeStatusCountProjection;
import com.inaing.blackhorse_erp.module.role.domain.Role;

public interface EmployeeRepository
        extends JpaRepository<Employee, String>, JpaSpecificationExecutor<Employee> {

    @Override
    Page<Employee> findAll(Specification<Employee> spec, Pageable pageable);

    // Counts honour the role scope of the list but ignore its other filters, so the
    // stat cards stay stable while the table is filtered.
    @Query("""
            SELECT e.status AS status, COUNT(e) AS count
            FROM Employee e
            WHERE e.role IN :roles
            GROUP BY e.status
            """)
    List<EmployeeStatusCountProjection> findStatusCounts(@Param("roles") List<Role> roles);

    Optional<Employee> findByPhone(String phone);

    Optional<Employee> findByCode(String code);

    boolean existsByPhone(String phone);

    boolean existsByRole(Role role);

    boolean existsByCode(String code);

    List<Employee> findByRole(Role role);
}
