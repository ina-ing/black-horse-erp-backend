package com.inaing.blackhorse_erp.module.warehouse.service;

import com.inaing.blackhorse_erp.module.warehouse.domain.Warehouse;

public interface IWarehouseService {

    Warehouse create(Warehouse warehouse);

    Warehouse update(Warehouse warehouse);

    Warehouse getByIdentifier(String identifier);

    String generateWarehouseCode();
}
