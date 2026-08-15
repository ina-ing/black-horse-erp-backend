package com.inaing.blackhorse_erp.module.warehouse.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.CodeType;
import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.warehouse.domain.Warehouse;
import com.inaing.blackhorse_erp.module.warehouse.repository.WarehouseRepository;
import com.inaing.blackhorse_erp.module.warehouse.service.IWarehouseService;
import com.inaing.blackhorse_erp.utils.generators.CodeGeneratorUtil;
import com.inaing.blackhorse_erp.utils.uuid.UUIDUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements IWarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Override
    @Transactional
    public Warehouse create(Warehouse warehouse) {
        if (warehouseRepository.existsByName(warehouse.getName())) {
            throw new AppException(ErrorCode.DUPLICATE_RESOURCE,
                    "Warehouse name already exists" + warehouse.getName());
        }

        warehouse.setCode(CodeGeneratorUtil.generateCode(CodeType.WAREHOUSE));
        return warehouseRepository.save(warehouse);
    }

    @Override
    @Transactional
    public Warehouse update(Warehouse warehouse) {
        warehouseRepository.findByName(warehouse.getName())
                .filter(existing -> !existing.getId().equals(warehouse.getId()))
                .ifPresent(existing -> {
                    throw new AppException(ErrorCode.DUPLICATE_RESOURCE,
                            "Warehouse name already exists " + warehouse.getName());
                });

        return warehouseRepository.save(warehouse);
    }

    @Override
    @Transactional(readOnly = true)
    public Warehouse getByIdentifier(String identifier) {
        if (UUIDUtils.isUUID(identifier)) {
            return warehouseRepository.findById(identifier).orElse(null);
        }
        return warehouseRepository.findByCode(identifier).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Warehouse getByManagerId(String managerId) {
        return warehouseRepository.findByManagerId(managerId).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Warehouse getWarehouse() {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        if (warehouses.size() != 1) {
            throw new AppException(ErrorCode.MULTIPLE_WAREHOUSES_NOT_SUPPORTED,
                    "Expected exactly one warehouse, found: " + warehouses.size());
        }
        return warehouses.get(0);
    }
}
