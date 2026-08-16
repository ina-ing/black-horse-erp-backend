package com.inaing.blackhorse_erp.module.backlog.dto.response;

public record BacklogItemResponseDto(

        String id,
        String articleCode,
        String articleName,
        String variantSizeId,
        String color,
        String sku,
        String size,
        Integer quantity) {

}
