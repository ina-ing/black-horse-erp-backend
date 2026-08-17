package com.inaing.blackhorse_erp.module.employee.usecase;

import java.util.List;

import com.inaing.blackhorse_erp.common.dto.list.PagedListWithAnalytics;
import com.inaing.blackhorse_erp.module.employee.dto.response.analytics.EmployeeBasicAnalyticsDto;
import com.inaing.blackhorse_erp.module.employee.dto.EmployeeResponseDto;
import com.inaing.blackhorse_erp.module.employee.dto.request.EmployeeCreationRequestDto;
import com.inaing.blackhorse_erp.module.employee.dto.request.EmployeeQueryParams;
import com.inaing.blackhorse_erp.module.employee.dto.request.EmployeeUpdateRequestDto;
import com.inaing.blackhorse_erp.module.role.domain.Role;

public interface IEmployeeUseCases {
    EmployeeResponseDto create(EmployeeCreationRequestDto request);

    PagedListWithAnalytics<EmployeeBasicAnalyticsDto, EmployeeResponseDto> getAllEmployees(
            EmployeeQueryParams params);

    List<EmployeeResponseDto> getEmployeesByRole(Role role);

    EmployeeResponseDto getByIdentifier(String identifier);

    EmployeeResponseDto update(String identifier, EmployeeUpdateRequestDto request);
}
