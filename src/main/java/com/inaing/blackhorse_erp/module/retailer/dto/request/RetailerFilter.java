package com.inaing.blackhorse_erp.module.retailer.dto.request;

import com.inaing.blackhorse_erp.common.domain.Province;
import com.inaing.blackhorse_erp.module.retailer.domain.enums.BusinessType;

// Resolved, typed filter passed into the service (spec is built from this).
// assignedSalesmanId carries the role scoping of the caller.
public record RetailerFilter(
        Province province,
        BusinessType businessType,
        String search,
        String assignedSalesmanId) {
}
