package com.inaing.blackhorse_erp.module.employee.dto.response.analytics;

import java.util.Map;

import com.inaing.blackhorse_erp.module.employee.domain.EmployeeStatus;

public record EmployeeBasicAnalyticsDto(long total, Map<EmployeeStatus, Long> byStatus) {
}
