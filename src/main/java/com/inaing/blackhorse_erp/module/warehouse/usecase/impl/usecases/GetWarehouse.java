package com.inaing.blackhorse_erp.module.warehouse.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.warehouse.domain.Warehouse;
import com.inaing.blackhorse_erp.module.warehouse.dto.response.WarehouseResponseDto;
import com.inaing.blackhorse_erp.module.warehouse.mapper.WarehouseMapper;
import com.inaing.blackhorse_erp.module.warehouse.service.IWarehouseService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetWarehouse {

    private final WarehouseMapper warehouseMapper;
    private final IWarehouseService warehouseService;

    @Transactional(readOnly = true)
    public WarehouseResponseDto execute(String identifier) {

        Warehouse warehouse = warehouseService.getByIdentifier(identifier);
        if (warehouse == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Warehouse not found " + identifier);
        }

        return warehouseMapper.toResponse(warehouse);
    }
}
