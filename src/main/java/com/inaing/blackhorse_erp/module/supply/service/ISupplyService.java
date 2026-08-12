package com.inaing.blackhorse_erp.module.supply.service;

import java.util.List;

import com.inaing.blackhorse_erp.module.supply.domain.Supply;

public interface ISupplyService {

    Supply create(Supply supply);

    Supply update(Supply supply);

    Supply getByIdentifier(String identifier);

    List<Supply> getAll();
}
