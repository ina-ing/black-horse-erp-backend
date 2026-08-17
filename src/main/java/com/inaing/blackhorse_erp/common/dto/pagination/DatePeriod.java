package com.inaing.blackhorse_erp.common.dto.pagination;

public enum DatePeriod {
    TODAY,
    YESTERDAY,
    LAST_7_DAYS,
    LAST_30_DAYS,
    LAST_6_MONTHS,
    LAST_12_MONTHS,
    THIS_MONTH,
    DATE,
    RANGE;

    public static DatePeriod fromNullable(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return DatePeriod.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
