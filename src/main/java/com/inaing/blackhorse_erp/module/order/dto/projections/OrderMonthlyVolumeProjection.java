package com.inaing.blackhorse_erp.module.order.dto.projections;

public interface OrderMonthlyVolumeProjection {

    int getYear();

    int getMonth();

    long getQuantity();
}
