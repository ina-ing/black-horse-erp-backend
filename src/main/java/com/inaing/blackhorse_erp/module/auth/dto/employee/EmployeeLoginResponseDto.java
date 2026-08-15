package com.inaing.blackhorse_erp.module.auth.dto.employee;

import com.inaing.blackhorse_erp.module.auth.dto.LoginUserDto;

public record EmployeeLoginResponseDto(
        String id,
        String code,
        String name,
        String phone,
        String role) implements LoginUserDto {

}
