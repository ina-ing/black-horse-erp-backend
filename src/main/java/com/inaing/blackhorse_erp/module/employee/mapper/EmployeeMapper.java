package com.inaing.blackhorse_erp.module.employee.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.dto.EmployeeCreationRequestDto;
import com.inaing.blackhorse_erp.module.employee.dto.EmployeeResponseDto;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeResponseDto toResponse(Employee employee);

    @Mapping(target = "passwordHash", source = "password")
    Employee toEntity(EmployeeCreationRequestDto request);
}
