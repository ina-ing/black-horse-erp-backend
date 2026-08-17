package com.inaing.blackhorse_erp.module.supply.dto.projections;

import com.inaing.blackhorse_erp.module.supply.domain.enums.SupplyStatus;

public interface SupplyStatusCountProjection {

    SupplyStatus getStatus();

    long getCount();
}
