package com.inaing.blackhorse_erp.module.returns.dto.response;

public record ReturnItemsResponseDto(

        String id,
        String articleName,
        String variantSizeId,
        String color,
        String sku,
        String size,
        Integer quantity) {

}
