package com.inaing.blackhorse_erp.module.employee.dto;

import com.inaing.blackhorse_erp.module.role.domain.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmployeeCreationRequestDto(
        @NotBlank @Size(max = 120) String fullname,
        @NotBlank @Size(min = 8, max = 100) String password,
        @NotBlank @Pattern(regexp = "\\+?\\d{7,15}", message = "must be a valid phone number") String phone,
        @Email @Size(max = 160) String email,
        @Size(max = 10) String panNumber,
        @NotNull Role role,
        @Size(max = 255) String address) {
}
