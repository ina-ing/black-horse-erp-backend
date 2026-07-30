package com.inaing.blackhorse_erp.module.retailer.dto.analytics;

import lombok.Builder;

@Builder
public record RetailerListAnalyticsDto(
        Long totalRetailers, Long newRetailers) {

}
