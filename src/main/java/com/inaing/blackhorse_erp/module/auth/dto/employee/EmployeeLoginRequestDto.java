package com.inaing.blackhorse_erp.module.auth.dto.employee;

import jakarta.validation.constraints.NotBlank;

public record EmployeeLoginRequestDto(
        @NotBlank String phone,
        @NotBlank String password) {
}
