package com.inaing.blackhorse_erp.module.production.dto.response;

import java.time.Instant;

public record ProductionListResponseDto(

        String id,
        String code,
        String factory,
        Instant productionDate,
        Integer totalArticles,
        Integer totalQuantity) {

}
