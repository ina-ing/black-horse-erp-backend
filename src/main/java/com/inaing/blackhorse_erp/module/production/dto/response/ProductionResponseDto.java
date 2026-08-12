package com.inaing.blackhorse_erp.module.production.dto.response;

import java.time.Instant;
import java.util.List;

public record ProductionResponseDto(

        String id,
        String code,
        String factory,
        Instant productionDate,
        Integer totalArticles,
        Integer totalQuantity,
        List<ProductionItemsResponseDto> items) {

}
