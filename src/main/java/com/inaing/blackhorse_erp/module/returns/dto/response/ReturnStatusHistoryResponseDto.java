package com.inaing.blackhorse_erp.module.returns.dto.response;

import java.time.Instant;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;

public record ReturnStatusHistoryResponseDto(

        String id,
        ReturnStatus status,
        ActionTrigger trigger,
        String actorName,
        String actorRole,
        Instant createdAt) {

}
