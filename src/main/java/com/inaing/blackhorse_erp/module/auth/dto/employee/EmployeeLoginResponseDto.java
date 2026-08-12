package com.inaing.blackhorse_erp.module.auth.dto.employee;

public record EmployeeLoginResponseDto(
        String id,
        String code,
        String name,
        String phone,
        String role) {

}
