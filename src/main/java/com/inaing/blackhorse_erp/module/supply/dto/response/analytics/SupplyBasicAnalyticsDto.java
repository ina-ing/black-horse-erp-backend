package com.inaing.blackhorse_erp.module.supply.dto.response.analytics;

import java.util.Map;

import com.inaing.blackhorse_erp.module.supply.domain.enums.SupplyStatus;

public record SupplyBasicAnalyticsDto(long total, Map<SupplyStatus, Long> byStatus) {
}
