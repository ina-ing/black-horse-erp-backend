package com.inaing.blackhorse_erp.module.inventory.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.inventory.domain.Inventory;
import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;
import com.inaing.blackhorse_erp.module.inventory.repository.InventoryRepository;
import com.inaing.blackhorse_erp.module.inventory.service.IInventoryService;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements IInventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public Inventory create(Inventory inventory) {
        if (inventoryRepository.existsByLocationTypeAndReference(inventory.getLocationType(),
                inventory.getReference())) {
            throw new AppException(ErrorCode.DUPLICATE_RESOURCE,
                    "Inventory already exists for " + inventory.getReference());
        }
        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public Inventory createFor(LocationType locationType, String referenceId) {
        return create(Inventory.builder()
                .locationType(locationType)
                .reference(referenceId)
                .build());
    }

    @Override
    @Transactional
    public int credit(LocationType referenceType, String reference, Map<ProductVariantSize, Integer> quantities) {
        findInventoryOrThrow(referenceType, reference).addQuantities(quantities);
        return 1;
    }

    @Override
    @Transactional
    public int debit(LocationType referenceType, String reference, Map<ProductVariantSize, Integer> quantities) {
        findInventoryOrThrow(referenceType, reference).reduceQuantities(quantities);
        return 1;
    }

    @Override
    @Transactional
    public Inventory adjust(LocationType locationType, String reference, Map<ProductVariantSize, Integer> quantities) {
        Inventory inventory = findInventoryOrThrow(locationType, reference);
        inventory.setQuantities(quantities);
        return inventory;
    }

    @Override
    @Transactional(readOnly = true)
    public Inventory getByLocation(LocationType locationType, String reference) {
        return findInventoryOrThrow(locationType, reference);
    }

    @Override
    @Transactional
    public int transfer(LocationType fromType, String fromReference,
            LocationType toType, String toReference,
            Map<ProductVariantSize, Integer> quantities) {
        debit(fromType, fromReference, quantities);
        credit(toType, toReference, quantities);
        return 1;
    }

    private Inventory findInventoryOrThrow(LocationType referenceType, String reference) {
        return inventoryRepository.findByLocationTypeAndReference(referenceType, reference)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_NOT_FOUND,
                        "Inventory not found for reference: " + reference));
    }
}
