package com.inaing.blackhorse_erp.module.supply.usecase.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyCreationRequestDto;
import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyItemsUpdateRequestDto;
import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.common.dto.list.PagedListWithAnalytics;
import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyQueryParams;
import com.inaing.blackhorse_erp.module.supply.dto.response.SupplyListResponseDto;
import com.inaing.blackhorse_erp.module.supply.dto.response.SupplyResponseDto;
import com.inaing.blackhorse_erp.module.supply.dto.response.analytics.SupplyBasicAnalyticsDto;
import com.inaing.blackhorse_erp.module.supply.usecase.ISupplyUsecases;
import com.inaing.blackhorse_erp.module.supply.usecase.impl.usecases.CreateSupplyUsecase;
import com.inaing.blackhorse_erp.module.supply.usecase.impl.usecases.GetAllSupplyUsecase;
import com.inaing.blackhorse_erp.module.supply.usecase.impl.usecases.GetSupplyUsecase;
import com.inaing.blackhorse_erp.module.supply.usecase.impl.usecases.UpdateSupplyItemsUsecase;
import com.inaing.blackhorse_erp.module.supply.usecase.impl.usecases.UpdateSupplyStatusUsecase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SupplyUsecasesImpl implements ISupplyUsecases {

    private final CreateSupplyUsecase createSupplyUsecase;
    private final GetSupplyUsecase getSupplyUsecase;
    private final GetAllSupplyUsecase getAllSupplyUsecase;
    private final UpdateSupplyStatusUsecase updateSupplyStatusUsecase;
    private final UpdateSupplyItemsUsecase updateSupplyItemsUsecase;

    @Override
    public SupplyResponseDto create(SupplyCreationRequestDto request) {
        return createSupplyUsecase.execute(request);
    }

    @Override
    public SupplyResponseDto getByIdentifier(String identifier) {
        return getSupplyUsecase.execute(identifier);
    }

    @Override
    public PagedListWithAnalytics<SupplyBasicAnalyticsDto, SupplyListResponseDto> getAll(
            SupplyQueryParams params) {
        return getAllSupplyUsecase.execute(params);
    }

    @Override
    public SupplyResponseDto updateStatus(String identifier, SupplyStatusUpdateRequestDto request) {
        return updateSupplyStatusUsecase.execute(identifier, request);
    }

    @Override
    public SupplyResponseDto updateItems(String identifier, SupplyItemsUpdateRequestDto request) {
        return updateSupplyItemsUsecase.execute(identifier, request);
    }

}
