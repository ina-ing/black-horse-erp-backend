package com.inaing.blackhorse_erp.module.warehouse.dto.response;

import java.time.Instant;

import com.inaing.blackhorse_erp.common.domain.Province;

public record WarehouseResponseDto(

        String id,
        String code,
        String name,
        String address,
        Province province,
        String phone,
        String email,
        String manager,
        Instant createdAt) {

}
