package com.inaing.blackhorse_erp.module.retailer.usecase.impl;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.common.dto.list.ListDtoWithAnalytics;
import com.inaing.blackhorse_erp.module.retailer.dto.analytics.RetailerListAnalyticsDto;
import com.inaing.blackhorse_erp.module.retailer.dto.request.RetailerCreationRequestDto;
import com.inaing.blackhorse_erp.module.retailer.dto.response.RetailerResponseDto;
import com.inaing.blackhorse_erp.module.retailer.usecase.IRetailerUsecase;
import com.inaing.blackhorse_erp.module.retailer.usecase.impl.usecases.CreateRetailerUsecase;
import com.inaing.blackhorse_erp.module.retailer.usecase.impl.usecases.GetRetailerByIdentifierUseCase;
import com.inaing.blackhorse_erp.module.retailer.usecase.impl.usecases.GetRetailersListUsecase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RetailerUsecaseImpl implements IRetailerUsecase {

    private final CreateRetailerUsecase createRetailerUsecase;
    private final GetRetailersListUsecase getAllRetailersUsecase;
    private final GetRetailerByIdentifierUseCase getRetailerByIdentifierUseCase;

    @Override
    public RetailerResponseDto create(RetailerCreationRequestDto request) {
        return createRetailerUsecase.execute(request);
    }

    @Override
    public ListDtoWithAnalytics<RetailerListAnalyticsDto, RetailerResponseDto> getAllRetailers() {
        return getAllRetailersUsecase.execute();
    }

    @Override
    public RetailerResponseDto getByIdentifier(String identifier) {
        return getRetailerByIdentifierUseCase.execute(identifier);
    }

}
