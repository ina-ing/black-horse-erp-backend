package com.inaing.blackhorse_erp.module.supply.usecase;

import java.util.List;

import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyCreationRequestDto;
import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyItemsUpdateRequestDto;
import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.common.dto.list.PagedListWithAnalytics;
import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyQueryParams;
import com.inaing.blackhorse_erp.module.supply.dto.response.SupplyListResponseDto;
import com.inaing.blackhorse_erp.module.supply.dto.response.SupplyResponseDto;
import com.inaing.blackhorse_erp.module.supply.dto.response.analytics.SupplyBasicAnalyticsDto;

public interface ISupplyUsecases {

    SupplyResponseDto create(SupplyCreationRequestDto request);

    SupplyResponseDto getByIdentifier(String identifier);

    PagedListWithAnalytics<SupplyBasicAnalyticsDto, SupplyListResponseDto> getAll(
            SupplyQueryParams params);

    SupplyResponseDto updateStatus(String identifier, SupplyStatusUpdateRequestDto request);

    SupplyResponseDto updateItems(String identifier, SupplyItemsUpdateRequestDto request);
}
