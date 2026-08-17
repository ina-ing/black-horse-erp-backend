package com.inaing.blackhorse_erp.common.dto.pagination;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public final class DateRangeResolver {

    private DateRangeResolver() {
    }

    public static DateRange resolve(DatePeriod period, LocalDate date, LocalDate from, LocalDate to) {
        if (period == null) {
            return null;
        }
        LocalDate today = LocalDate.now();
        return switch (period) {
            case TODAY -> new DateRange(today, today);
            case YESTERDAY -> new DateRange(today.minusDays(1), today.minusDays(1));
            case LAST_7_DAYS -> new DateRange(today.minusDays(6), today);
            case LAST_30_DAYS -> new DateRange(today.minusDays(29), today);
            case LAST_6_MONTHS -> new DateRange(today.minusMonths(6), today);
            case LAST_12_MONTHS -> new DateRange(today.minusMonths(12), today);
            case THIS_MONTH -> new DateRange(
                    today.withDayOfMonth(1), today.with(TemporalAdjusters.lastDayOfMonth()));
            case DATE -> date == null ? null : new DateRange(date, date);
            case RANGE -> (from == null || to == null) ? null : new DateRange(from, to);
        };
    }
}
