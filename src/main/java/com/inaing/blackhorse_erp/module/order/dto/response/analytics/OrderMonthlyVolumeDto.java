package com.inaing.blackhorse_erp.module.order.dto.response.analytics;

import com.inaing.blackhorse_erp.module.order.dto.projections.OrderMonthlyVolumeProjection;

public record OrderMonthlyVolumeDto(int year, int month, long quantity) {

    public static OrderMonthlyVolumeDto from(OrderMonthlyVolumeProjection projection) {
        return new OrderMonthlyVolumeDto(
                projection.getYear(), projection.getMonth(), projection.getQuantity());
    }
}
