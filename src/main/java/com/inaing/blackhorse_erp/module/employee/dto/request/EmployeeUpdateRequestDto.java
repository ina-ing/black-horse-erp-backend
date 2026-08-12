package com.inaing.blackhorse_erp.module.employee.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmployeeUpdateRequestDto(
        @Size(max = 120) String fullname,
        @Pattern(regexp = "\\+?\\d{7,15}", message = "must be a valid phone number") String phone,
        @Email @Size(max = 160) String email,
        @Size(max = 10) String panNumber,
        String role,
        @Size(max = 255) String address,
        String status) {

}
