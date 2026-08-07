package com.inaing.blackhorse_erp.module.warehouse.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        warehouse.setCode(this.generateWarehouseCode());
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
    public String generateWarehouseCode() {
        for (int attempt = 0; attempt < 10; attempt++) {
            String code = CodeGeneratorUtil.generate("WH-", 4);

            if (!warehouseRepository.existsByCode(code)) {
                return code;
            }
        }
        throw new AppException(ErrorCode.IDENTIFIER_ALREADY_EXISTS);
    }

    @Override
    @Transactional(readOnly = true)
    public Warehouse getByIdentifier(String identifier) {
        if (UUIDUtils.isUUID(identifier)) {
            return warehouseRepository.findById(identifier).orElse(null);
        }
        return warehouseRepository.findByCode(identifier).orElse(null);
    }

}
