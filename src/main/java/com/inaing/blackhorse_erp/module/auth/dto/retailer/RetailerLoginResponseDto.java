package com.inaing.blackhorse_erp.module.auth.dto.retailer;

import com.inaing.blackhorse_erp.module.auth.dto.LoginUserDto;

public record RetailerLoginResponseDto(
        String id,
        String code,
        String storeName,
        String phone,
        String contactPerson,
        String panNumber,
        String assignedSalesman,
        String role) implements LoginUserDto {

}
