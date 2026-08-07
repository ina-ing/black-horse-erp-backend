package com.inaing.blackhorse_erp.module.returns.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record ReturnCreationRequestDto(

        @NotBlank String retailer,
        String note,
        @NotBlank String reason,
        @NotEmpty @Valid List<ReturnItemRequestDto> items) {

}
