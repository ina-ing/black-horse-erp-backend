package com.inaing.blackhorse_erp.module.auth.dto.retailer;

import jakarta.validation.constraints.NotBlank;

public record RetailerLoginRequestDto(
        @NotBlank String phone,
        @NotBlank String password) {

}
