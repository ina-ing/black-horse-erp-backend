package com.inaing.blackhorse_erp.module.warehouse.usecase;

import com.inaing.blackhorse_erp.module.warehouse.domain.Warehouse;
import com.inaing.blackhorse_erp.module.warehouse.dto.request.WarehouseRequestDto;
import com.inaing.blackhorse_erp.module.warehouse.dto.response.WarehouseResponseDto;

public interface IWarehouseUsecases {

    WarehouseResponseDto create(WarehouseRequestDto request);

    WarehouseResponseDto update(String identifier, WarehouseRequestDto request);

    Warehouse getByIdentifier(String identifier);
}
