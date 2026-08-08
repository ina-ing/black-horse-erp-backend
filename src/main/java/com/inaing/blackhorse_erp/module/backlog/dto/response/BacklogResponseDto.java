package com.inaing.blackhorse_erp.module.backlog.dto.response;

import java.util.List;

public record BacklogResponseDto(
        String id,
        String factory,
        Integer totalArticles,
        Integer totalQuantity,
        List<BacklogItemResponseDto> items) {

}
