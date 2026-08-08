package com.inaing.blackhorse_erp.module.production.dto.response;

import java.time.LocalDate;
import java.util.List;

public record ProductionResponseDto(

        String id,
        String code,
        String factory,
        LocalDate productionDate,
        Integer totalArticles,
        Integer totalQuantity,
        List<ProductionItemsResponseDto> items) {

}
