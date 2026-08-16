package com.inaing.blackhorse_erp.module.supply.dto.response;

public record SupplyItemResponseDto(

        String id,
        String articleCode,
        String articleName,
        String variantSizeId,
        String color,
        String sku,
        String size,
        Integer quantity) {

}
