package com.inaing.blackhorse_erp.common.dto.pagination;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public record DateRange(LocalDate from, LocalDate to) {

    public Instant startInstant() {
        return from.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    public Instant endInstant() {
        return to.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant();
    }
}
