package com.inaing.blackhorse_erp.module.productionOrder.dto.response;

import java.time.Instant;
import java.util.List;

import com.inaing.blackhorse_erp.module.productionOrder.domain.enums.ProductionOrderStatus;

public record ProductionOrderResponseDto(

        String id,
        String code,
        String warehouse,
        ProductionOrderStatus status,
        Instant orderDate,
        Integer totalArticles,
        Integer totalQuantity,
        List<ProductionOrderItemsResponseDto> items) {

}
