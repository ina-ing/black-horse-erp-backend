package com.inaing.blackhorse_erp.module.returns.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record ReturnUpdateRequestDto(
        String note,
        String reason,
        @NotEmpty @Valid List<ReturnItemRequestDto> items) {
}
