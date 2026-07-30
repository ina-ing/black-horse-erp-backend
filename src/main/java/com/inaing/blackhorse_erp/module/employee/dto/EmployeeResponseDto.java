package com.inaing.blackhorse_erp.module.employee.dto;

import java.time.Instant;
import java.time.LocalDate;

import com.inaing.blackhorse_erp.module.employee.domain.EmployeeStatus;

public record EmployeeResponseDto(
        String id,
        String code,
        String fullname,
        String phone,
        String email,
        String panNumber,
        String role,
        String address,
        LocalDate joinedOn,
        EmployeeStatus status,
        Instant createdAt) {

}
