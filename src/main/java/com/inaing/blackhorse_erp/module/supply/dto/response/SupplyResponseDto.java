package com.inaing.blackhorse_erp.module.supply.dto.response;

import java.time.Instant;
import java.util.List;

import com.inaing.blackhorse_erp.module.supply.domain.enums.SupplyStatus;

public record SupplyResponseDto(

        String id,
        String code,
        String suppliedBy,
        String suppliedTo,
        SupplyStatus status,
        Instant supplyDate,
        Integer totalArticles,
        Integer totalQuantity,
        List<SupplyItemResponseDto> items) {

}
