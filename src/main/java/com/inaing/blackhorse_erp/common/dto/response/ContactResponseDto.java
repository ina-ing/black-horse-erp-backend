package com.inaing.blackhorse_erp.common.dto.response;

public record ContactResponseDto(
        String id,
        String fullname,
        String phone,
        String email,
        String address) {

}
