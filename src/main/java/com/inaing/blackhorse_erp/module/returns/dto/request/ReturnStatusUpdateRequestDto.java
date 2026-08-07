package com.inaing.blackhorse_erp.module.returns.dto.request;

import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;

import jakarta.validation.constraints.NotNull;

public record ReturnStatusUpdateRequestDto(
        @NotNull ReturnStatus status) {

}
