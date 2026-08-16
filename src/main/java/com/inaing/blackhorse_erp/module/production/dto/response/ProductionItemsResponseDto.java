package com.inaing.blackhorse_erp.module.production.dto.response;

public record ProductionItemsResponseDto(

        String id,
        String articleCode,
        String articleName,
        String variantSizeId,
        String color,
        String sku,
        String size,
        Integer quantity) {

}
