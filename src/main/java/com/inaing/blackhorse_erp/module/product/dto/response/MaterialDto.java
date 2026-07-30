package com.inaing.blackhorse_erp.module.product.dto.response;

import jakarta.validation.constraints.NotBlank;

public record MaterialDto(
        @NotBlank String upperMaterial,

        @NotBlank String liningMaterial,

        @NotBlank String soleMaterial,

        @NotBlank String insoleMaterial) {

}
