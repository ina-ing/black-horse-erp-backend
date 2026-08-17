package com.inaing.blackhorse_erp.module.supply.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inaing.blackhorse_erp.module.supply.domain.Supply;
import com.inaing.blackhorse_erp.module.supply.dto.projections.SupplyStatusCountProjection;
import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyFilter;

public interface ISupplyService {

    Supply create(Supply supply);

    Supply update(Supply supply);

    Supply getByIdentifier(String identifier);

    List<Supply> getAll();

    Page<Supply> getSupplies(SupplyFilter filter, Pageable pageable);

    List<SupplyStatusCountProjection> getStatusCounts();
}
