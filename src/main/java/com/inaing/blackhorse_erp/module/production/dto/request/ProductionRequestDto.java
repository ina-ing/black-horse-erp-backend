package com.inaing.blackhorse_erp.module.production.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record ProductionRequestDto(

        @NotBlank String factory,
        @NotEmpty @Valid List<ProductionItemRequestDto> items) {

}
