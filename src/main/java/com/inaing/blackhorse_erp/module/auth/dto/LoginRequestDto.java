package com.inaing.blackhorse_erp.module.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank String phone,
        @NotBlank String password) {
}
