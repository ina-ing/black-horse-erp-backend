package com.inaing.blackhorse_erp.module.warehouse.usecase;

import com.inaing.blackhorse_erp.module.warehouse.dto.request.WarehouseCreationRequestDto;
import com.inaing.blackhorse_erp.module.warehouse.dto.request.WarehouseUpdateRequestDto;
import com.inaing.blackhorse_erp.module.warehouse.dto.response.WarehouseResponseDto;

public interface IWarehouseUsecases {

    WarehouseResponseDto create(WarehouseCreationRequestDto request);

    WarehouseResponseDto update(String identifier, WarehouseUpdateRequestDto request);

    WarehouseResponseDto getByIdentifier(String identifier);
}
