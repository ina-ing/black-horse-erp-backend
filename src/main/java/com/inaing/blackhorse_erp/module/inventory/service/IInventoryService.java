package com.inaing.blackhorse_erp.module.inventory.service;

import java.util.Map;

import com.inaing.blackhorse_erp.module.inventory.domain.Inventory;
import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;

public interface IInventoryService {

    Inventory create(Inventory inventory);

    Inventory createFor(LocationType locationType, String referenceId);

    int credit(LocationType referenceType, String reference, Map<ProductVariantSize, Integer> quantities);

    int debit(LocationType referenceType, String reference, Map<ProductVariantSize, Integer> quantities);

    Inventory adjust(LocationType locationType, String reference, Map<ProductVariantSize, Integer> quantities);

    Inventory getByLocation(LocationType locationType, String reference);

    public int transfer(LocationType fromType, String fromReference,
                         LocationType toType, String toReference,
                         Map<ProductVariantSize, Integer> quantities);
}
