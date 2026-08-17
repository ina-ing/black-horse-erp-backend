package com.inaing.blackhorse_erp.module.production.dto.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.inaing.blackhorse_erp.common.dto.request.PageableRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductionQueryParams extends PageableRequest {

    private String period;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;

    private String factory;
}
