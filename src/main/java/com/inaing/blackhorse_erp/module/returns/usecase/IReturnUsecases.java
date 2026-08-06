package com.inaing.blackhorse_erp.module.returns.usecase;

import java.util.List;

import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnCreationRequestDto;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnUpdateRequestDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnResponseDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnWithStatusHistoryResponseDto;

public interface IReturnUsecases {

    ReturnResponseDto create(ReturnCreationRequestDto request);

    ReturnWithStatusHistoryResponseDto getByIdentifier(String code);

    List<ReturnResponseDto> getAll();

    ReturnResponseDto update(String id, ReturnUpdateRequestDto request);

    ReturnResponseDto updateStatus(String id, ReturnStatusUpdateRequestDto request);

    ReturnResponseDto acceptStock(String id);
}
