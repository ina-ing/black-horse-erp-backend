package com.inaing.blackhorse_erp.module.retailer.usecase.impl.usecases;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.list.ListDtoWithAnalytics;
import com.inaing.blackhorse_erp.module.retailer.dto.analytics.RetailerListAnalyticsDto;
import com.inaing.blackhorse_erp.module.retailer.dto.response.RetailerResponseDto;
import com.inaing.blackhorse_erp.module.retailer.mapper.RetailerMapper;
import com.inaing.blackhorse_erp.module.retailer.service.IRetailerService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetRetailersListUsecase {

    private final IRetailerService retailerService;
    private final RetailerMapper retailerMapper;

    @Transactional(readOnly = true)
    public ListDtoWithAnalytics<RetailerListAnalyticsDto, RetailerResponseDto> execute() {

        List<RetailerResponseDto> retailers = retailerService.getAll().stream()
                .map(retailerMapper::toResponse).toList();

        Map<String, Long> count = retailerService.getRetailerCounts();
        RetailerListAnalyticsDto analyticsDto = RetailerListAnalyticsDto
                .builder()
                .totalRetailers(count.get("TOTAL"))
                .newRetailers(count.get("NEW"))
                .build();

        return ListDtoWithAnalytics.of(analyticsDto, retailers);
    }
}
