package com.inaing.blackhorse_erp.module.production.dto.response;

public record ProductionItemsResponseDto(

        String id,
        String articleName,
        String variantSizeId,
        String color,
        String sku,
        String size,
        Integer quantity) {

}
