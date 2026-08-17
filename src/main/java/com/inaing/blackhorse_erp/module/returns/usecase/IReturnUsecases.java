package com.inaing.blackhorse_erp.module.returns.usecase;

import com.inaing.blackhorse_erp.common.dto.list.PagedListWithAnalytics;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnCreationRequestDto;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnQueryParams;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnUpdateRequestDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnListResponseDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.analytics.ReturnBasicAnalyticsDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnResponseDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnWithStatusHistoryResponseDto;

public interface IReturnUsecases {

    ReturnResponseDto create(ReturnCreationRequestDto request);

    ReturnWithStatusHistoryResponseDto getByIdentifier(String code);

    PagedListWithAnalytics<ReturnBasicAnalyticsDto, ReturnListResponseDto> getAll(ReturnQueryParams params);

    ReturnResponseDto update(String id, ReturnUpdateRequestDto request);

    ReturnResponseDto updateStatus(String id, ReturnStatusUpdateRequestDto request);

    ReturnResponseDto acceptStock(String id);
}
