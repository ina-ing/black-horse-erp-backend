package com.inaing.blackhorse_erp.module.returns.dto.projections;

import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;

public interface ReturnStatusCountProjection {

    ReturnStatus getStatus();

    long getCount();
}
