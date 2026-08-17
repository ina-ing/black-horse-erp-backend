package com.inaing.blackhorse_erp.module.employee.usecase.impl.usecases;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.list.PagedListWithAnalytics;
import com.inaing.blackhorse_erp.common.dto.pagination.PageableFactory;
import com.inaing.blackhorse_erp.common.dto.response.PageResponse;
import com.inaing.blackhorse_erp.module.employee.domain.EmployeeStatus;
import com.inaing.blackhorse_erp.module.employee.dto.EmployeeResponseDto;
import com.inaing.blackhorse_erp.module.employee.dto.request.EmployeeFilter;
import com.inaing.blackhorse_erp.module.employee.dto.request.EmployeeQueryParams;
import com.inaing.blackhorse_erp.module.employee.dto.response.analytics.EmployeeBasicAnalyticsDto;
import com.inaing.blackhorse_erp.module.employee.mapper.EmployeeMapper;
import com.inaing.blackhorse_erp.module.employee.service.IEmployeeService;
import com.inaing.blackhorse_erp.module.role.domain.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetAllEmployeesUseCase {

    private static final Map<String, String> SORT_ALLOWLIST = Map.of(
            "joinedOn", "joinedOn",
            "fullname", "fullname");

    private static final List<Role> ALL_ROLES = Arrays.asList(Role.values());

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "joinedOn");

    private final IEmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @Transactional(readOnly = true)
    public PagedListWithAnalytics<EmployeeBasicAnalyticsDto, EmployeeResponseDto> execute(
            EmployeeQueryParams params) {

        Pageable pageable = PageableFactory.from(params, SORT_ALLOWLIST, DEFAULT_SORT);

        EmployeeFilter filter = new EmployeeFilter(
                params.getRoles(), params.getStatus(), params.getSearch());

        Page<EmployeeResponseDto> page = employeeService.getEmployees(filter, pageable)
                .map(employeeMapper::toResponse);

        return PagedListWithAnalytics.of(analytics(params.getRoles()), PageResponse.of(page));
    }

    // Scoped to the requested roles so a role-specific list (salesmen) counts only
    // its own people, but never narrowed by the search or status filter.
    private EmployeeBasicAnalyticsDto analytics(List<Role> roles) {

        Map<EmployeeStatus, Long> byStatus = new EnumMap<>(EmployeeStatus.class);
        for (EmployeeStatus status : EmployeeStatus.values()) {
            byStatus.put(status, 0L);
        }
        employeeService.getStatusCounts(roles == null || roles.isEmpty() ? ALL_ROLES : roles)
                .forEach(count -> byStatus.put(count.getStatus(), count.getCount()));

        long total = byStatus.values().stream().mapToLong(Long::longValue).sum();

        return new EmployeeBasicAnalyticsDto(total, byStatus);
    }
}
