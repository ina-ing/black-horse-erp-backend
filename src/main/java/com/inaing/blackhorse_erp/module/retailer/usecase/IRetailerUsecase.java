package com.inaing.blackhorse_erp.module.retailer.usecase;

import java.util.List;

import com.inaing.blackhorse_erp.common.dto.list.PagedListWithAnalytics;
import com.inaing.blackhorse_erp.module.retailer.dto.request.RetailerQueryParams;
import com.inaing.blackhorse_erp.module.employee.dto.EmployeeResponseDto;
import com.inaing.blackhorse_erp.module.retailer.dto.analytics.RetailerListAnalyticsDto;
import com.inaing.blackhorse_erp.module.retailer.dto.request.RetailerCreationRequestDto;
import com.inaing.blackhorse_erp.module.retailer.dto.request.RetailerUpdateRequestDto;
import com.inaing.blackhorse_erp.module.retailer.dto.response.RetailerDetailsResponseDto;
import com.inaing.blackhorse_erp.module.retailer.dto.response.RetailerResponseDto;

public interface IRetailerUsecase {
    RetailerResponseDto create(RetailerCreationRequestDto request);

    PagedListWithAnalytics<RetailerListAnalyticsDto, RetailerResponseDto> getAllRetailers(
            RetailerQueryParams params);

    RetailerDetailsResponseDto getByIdentifier(String identifier);

    List<RetailerResponseDto> getAssignedRetailers();

    EmployeeResponseDto getAssignedSalesman();

    RetailerResponseDto update(String identifier, RetailerUpdateRequestDto request);
}