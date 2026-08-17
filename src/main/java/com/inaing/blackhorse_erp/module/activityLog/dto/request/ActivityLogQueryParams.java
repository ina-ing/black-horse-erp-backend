package com.inaing.blackhorse_erp.module.activityLog.dto.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.inaing.blackhorse_erp.common.dto.request.PageableRequest;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityPriority;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityLogQueryParams extends PageableRequest {

    private String period;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;

    private ActivityPriority priority;

    private ActivityEntityType entityType;
}
