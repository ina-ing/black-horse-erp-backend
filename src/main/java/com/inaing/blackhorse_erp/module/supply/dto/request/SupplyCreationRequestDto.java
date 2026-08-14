package com.inaing.blackhorse_erp.module.supply.dto.request;

import java.util.List;

import com.inaing.blackhorse_erp.module.production.dto.request.ProductionItemRequestDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record SupplyCreationRequestDto(

                @NotBlank String suppliedBy,
                @NotBlank String suppliedTo,
                @NotEmpty List<@Valid ProductionItemRequestDto> items) {

}
