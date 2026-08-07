package com.inaing.blackhorse_erp.module.returns.dto.response;

import java.time.Instant;
import java.util.List;

import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnReason;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;

public record ReturnWithStatusHistoryResponseDto(

        String id,
        String code,
        String retailer,
        String handledBy,
        ReturnReason reason,
        ReturnStatus status,
        Instant returnDate,
        Integer totalArticles,
        Integer totalQuantity,
        String note, 
        List<ReturnItemsResponseDto> items,
        List<ReturnStatusHistoryResponseDto> statusHistory) {
}
