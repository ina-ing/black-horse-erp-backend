package com.inaing.blackhorse_erp.module.retailer.usecase;

import com.inaing.blackhorse_erp.common.dto.list.ListDtoWithAnalytics;
import com.inaing.blackhorse_erp.module.retailer.dto.analytics.RetailerListAnalyticsDto;
import com.inaing.blackhorse_erp.module.retailer.dto.request.RetailerCreationRequestDto;
import com.inaing.blackhorse_erp.module.retailer.dto.response.RetailerResponseDto;

public interface IRetailerUsecase {
    RetailerResponseDto create(RetailerCreationRequestDto request);

    ListDtoWithAnalytics<RetailerListAnalyticsDto, RetailerResponseDto> getAllRetailers();

    RetailerResponseDto getByIdentifier(String identifier);
}