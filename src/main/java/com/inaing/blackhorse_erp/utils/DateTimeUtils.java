package com.inaing.blackhorse_erp.utils;

import java.sql.Date;
import java.time.LocalDate;

public class DateTimeUtils {
    public static LocalDate getCurrentDateAsSql() {
        return new Date(System.currentTimeMillis()).toLocalDate();
    }
}
