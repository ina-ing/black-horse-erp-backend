package com.inaing.blackhorse_erp.module.factory.dto.request;

import com.inaing.blackhorse_erp.common.domain.Province;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FactoryRequestDto(

        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 255) String address,
        @NotNull Province province,
        @NotBlank String manager) {
}