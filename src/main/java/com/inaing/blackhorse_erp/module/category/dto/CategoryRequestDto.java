package com.inaing.blackhorse_erp.module.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(

        @NotBlank String name) {
}
