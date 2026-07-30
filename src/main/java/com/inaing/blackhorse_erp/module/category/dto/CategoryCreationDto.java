package com.inaing.blackhorse_erp.module.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreationDto(
        @NotBlank String name) {

}
