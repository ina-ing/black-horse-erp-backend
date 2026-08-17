package com.inaing.blackhorse_erp.module.order.dto.request;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.inaing.blackhorse_erp.common.dto.request.PageableRequest;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderQueryParams extends PageableRequest {

    private String period;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;

    private List<OrderStatus> statuses;

    private String retailer;
}
