package com.inaing.blackhorse_erp.module.auth.dto.retailer;

public record RetailerLoginResponseDto(
        String id,
        String storeName,
        String phone,
        String contactPerson,
        String panNumber,
        String role) {

}
