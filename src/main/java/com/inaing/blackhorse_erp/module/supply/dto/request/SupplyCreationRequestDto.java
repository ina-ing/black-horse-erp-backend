package com.inaing.blackhorse_erp.module.supply.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record SupplyCreationRequestDto(

        @NotBlank String suppliedBy,
        @NotBlank String suppliedTo,
        @NotEmpty @Valid List<SupplyItemRequestDto> items) {

}
