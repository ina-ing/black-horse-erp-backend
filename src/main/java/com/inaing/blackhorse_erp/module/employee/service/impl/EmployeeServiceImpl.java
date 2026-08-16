package com.inaing.blackhorse_erp.module.employee.service.impl;

import com.inaing.blackhorse_erp.module.employee.repository.EmployeeRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.CodeType;
import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.domain.EmployeeStatus;
import com.inaing.blackhorse_erp.module.employee.service.IEmployeeService;
import com.inaing.blackhorse_erp.module.role.domain.Role;
import com.inaing.blackhorse_erp.utils.generators.CodeGeneratorUtil;
import com.inaing.blackhorse_erp.utils.uuid.UUIDUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements IEmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public Employee create(Employee employee) {
        if (employeeRepository.existsByPhone(employee.getPhone())) {
            throw new AppException(ErrorCode.DUPLICATE_PHONE,
                    "Phone number already registered " + employee.getPhone());
        }
        employee.setCode(generateEmployeeCode(employee.getRole()));
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setJoinedOn(LocalDate.now());
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getById(String id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getByIdentifier(String identifier) {

        if (UUIDUtils.isUUID(identifier)) {
            return employeeRepository.findById(identifier).orElse(null);
        }
        if (identifier.matches("^\\+?\\d{10,15}$")) {
            return employeeRepository.findByPhone(identifier).orElse(null);
        }
        return employeeRepository.findByCode(identifier).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findByPhone(String phone) {
        return employeeRepository.findByPhone(phone).orElse(null);
    }

    @Override
    public String generateEmployeeCode(Role role) {

        CodeType type = switch (role) {
            case ADMIN -> CodeType.ADMIN;
            case SALES -> CodeType.SALES;
            case WAREHOUSE -> CodeType.WAREHOUSE;
            case FACTORY -> CodeType.FACTORY;
            default -> throw new AppException(
                    ErrorCode.INVALID_ENUM_VALUE,
                    "Role " + role + " is not a valid employee role.");
        };

        return CodeGeneratorUtil.generateCode(type);
    }

    @Override
    @Transactional
    public Employee update(Employee employee) {
       return employeeRepository.save(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getByRole(Role role) {
        return employeeRepository.findByRole(role);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByRole(Role role) {
        return employeeRepository.existsByRole(role);
    }

}
