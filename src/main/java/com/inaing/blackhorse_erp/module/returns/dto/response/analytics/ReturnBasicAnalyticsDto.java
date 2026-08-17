package com.inaing.blackhorse_erp.module.returns.dto.response.analytics;

import java.util.Map;

import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;

public record ReturnBasicAnalyticsDto(long total, Map<ReturnStatus, Long> byStatus) {
}
