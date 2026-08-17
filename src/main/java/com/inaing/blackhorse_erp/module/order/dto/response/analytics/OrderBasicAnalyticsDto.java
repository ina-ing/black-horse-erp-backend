package com.inaing.blackhorse_erp.module.order.dto.response.analytics;

import java.util.List;
import java.util.Map;

import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;

public record OrderBasicAnalyticsDto(
        long total,
        Map<OrderStatus, Long> byStatus,
        List<OrderMonthlyVolumeDto> monthlyVolume) {
}
