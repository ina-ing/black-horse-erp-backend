package com.inaing.blackhorse_erp.module.productionOrder.dto.request;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.inaing.blackhorse_erp.common.dto.request.PageableRequest;
import com.inaing.blackhorse_erp.module.productionOrder.domain.enums.ProductionOrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductionOrderQueryParams extends PageableRequest {

    private String period;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;

    private List<ProductionOrderStatus> statuses;
}
